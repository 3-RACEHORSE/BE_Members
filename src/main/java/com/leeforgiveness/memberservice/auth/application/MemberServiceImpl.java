package com.leeforgiveness.memberservice.auth.application;

import com.leeforgiveness.memberservice.auth.domain.Member;
import com.leeforgiveness.memberservice.auth.domain.SnsInfo;
import com.leeforgiveness.memberservice.auth.domain.UserReport;
import com.leeforgiveness.memberservice.auth.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberInfoResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberReportRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSnsLoginRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUuidResponseDto;
import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.TokenResponseDto;
import com.leeforgiveness.memberservice.auth.infrastructure.MemberRepository;
import com.leeforgiveness.memberservice.auth.infrastructure.RefreshTokenCertification;
import com.leeforgiveness.memberservice.auth.infrastructure.SnsInfoRepository;

import com.leeforgiveness.memberservice.auth.infrastructure.UserReportRepository;
import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.common.exception.ResponseStatus;
import com.leeforgiveness.memberservice.common.security.JwtTokenProvider;
import com.leeforgiveness.memberservice.subscribe.infrastructure.SellerSubscriptionRepository;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final SnsInfoRepository snsInfoRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserReportRepository userReportRepository;
	private final SellerSubscriptionRepository sellerSubscriptionRepository;
	private final RefreshTokenCertification refreshTokenCertification;

	//이메일 중복 확인
	private void checkEmailDuplicate(String email) {
		if (memberRepository.findByEmail(email).isPresent()) {
			throw new CustomException(ResponseStatus.DUPLICATE_EMAIL);
		}
	}

	//휴대폰 번호 중복 확인
	private void checkPhoneNumberDuplicate(String phoneNum) {
		if (memberRepository.findByPhoneNum(phoneNum).isPresent()) {
			throw new CustomException(ResponseStatus.DUPLICATE_PHONE_NUMBER);
		}
	}

	//SNS 회원 추가
	@Override
	@Transactional
	public void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto) {
		if (snsInfoRepository.findBySnsIdAndSnsType(snsMemberAddRequestDto.getSnsId(),
			snsMemberAddRequestDto.getSnsType()).isPresent()) {
			throw new CustomException(ResponseStatus.DUPLICATED_MEMBERS);
		}

		//이메일 중복 확인
		checkEmailDuplicate(snsMemberAddRequestDto.getEmail());

		//휴대폰 번호 중복 확인
		checkPhoneNumberDuplicate(snsMemberAddRequestDto.getPhoneNum());

		String uuid = UUID.randomUUID().toString();

		Member member = Member.builder()
			.email(snsMemberAddRequestDto.getEmail())
			.name(snsMemberAddRequestDto.getName())
			.phoneNum(snsMemberAddRequestDto.getPhoneNum())
			.uuid(uuid)
			.profileImage(
				"https://ifh.cc/g/Vv1lrR.png")
			.build();

		memberRepository.save(member);

		SnsInfo snsInfo = SnsInfo.builder()
			.snsId(snsMemberAddRequestDto.getSnsId())
			.snsType(snsMemberAddRequestDto.getSnsType())
			.member(member)
			.build();

		snsInfoRepository.save(snsInfo);
	}

	//	토큰 생성
	private String createToken(Member member) {
		UserDetails userDetails = User.withUsername(member.getEmail()).password(member.getUuid())
			.roles("USER").build();
		return jwtTokenProvider.generateToken(userDetails);
	}

	//리프레쉬 토큰 생성
	private String createRefreshToken(Member member) {
		UserDetails userDetails = User.withUsername(member.getEmail()).password(member.getUuid())
			.roles("USER").build();
		return jwtTokenProvider.generateRefreshToken(userDetails);
	}

	//	소셜 로그인
	@Override
	@Transactional
	public TokenResponseDto snsLogin(MemberSnsLoginRequestDto memberSnsLoginRequestDto) {
		SnsInfo snsInfo = snsInfoRepository.findBySnsIdAndSnsType(
				memberSnsLoginRequestDto.getSnsId(), memberSnsLoginRequestDto.getSnsType())
			.orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));
		Member member = memberRepository.findByEmail(memberSnsLoginRequestDto.getEmail())
			.orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));
		if (member.isTerminationStatus()) {
			throw new CustomException(ResponseStatus.WITHDRAWAL_MEMBERS);
		}

		String token = createToken(member);
		String refreshToken = createRefreshToken(member);

		refreshTokenCertification.saveRefreshToken(member.getUuid(), refreshToken);

		return TokenResponseDto.builder()
			.accessToken(token)
			.refreshToken(refreshToken)
			.uuid(member.getUuid())
			.build();
	}

	//토큰 재발급
	@Override
	public TokenResponseDto tokenReIssue(String receiveToken, String uuid) {
		Member member = memberRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));
		if (member.isTerminationStatus()) {
			throw new CustomException(ResponseStatus.WITHDRAWAL_MEMBERS);
		}
		if (refreshTokenCertification.hasKey(uuid) && refreshTokenCertification.getRefreshToken(
			uuid).equals(receiveToken)) {
			String token = createToken(member);
			return TokenResponseDto.builder()
				.accessToken(token)
				.refreshToken(null)
				.uuid(member.getUuid())
				.build();
		} else {
			throw new CustomException(ResponseStatus.TOKEN_NOT_VALID);
		}
	}

	//회원정보 조회
	@Override
	public MemberDetailResponseDto findMember(String uuid) {
		Member member = memberRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ResponseStatus.NO_EXIST_MEMBERS));

		return MemberDetailResponseDto.builder()
			.email(member.getEmail())
			.name(member.getName())
			.phoneNum(member.getPhoneNum())
			.profileImage(member.getProfileImage())
			.build();
	}

	//회원정보 수정
	@Override
	@Transactional
	public void updateMember(String memberUuid,
		MemberUpdateRequestDto memberUpdateRequestDto) {
		Member member = memberRepository.findByUuid(memberUuid)
			.orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));

		//휴대폰번호 중복 확인
		if (!member.getPhoneNum().equals(memberUpdateRequestDto.getPhoneNum())) {
			checkPhoneNumberDuplicate(memberUpdateRequestDto.getPhoneNum());
		}

		memberRepository.save(Member.builder()
			.id(member.getId())
			.uuid(member.getUuid())
			.email(member.getEmail())
			.name(memberUpdateRequestDto.getName())
			.phoneNum(memberUpdateRequestDto.getPhoneNum())
			.profileImage(memberUpdateRequestDto.getProfileImage())
			.terminationStatus(member.isTerminationStatus())
			.build()
		);
	}

	//회원 탈퇴
	@Override
	@Transactional
	public void removeMember(String uuid) {
		Member member = memberRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));

		memberRepository.save(Member.builder()
			.id(member.getId())
			.uuid(member.getUuid())
			.email(member.getEmail())
			.name(member.getName())
			.phoneNum(member.getPhoneNum())
			.terminationStatus(true)
			.profileImage(member.getProfileImage())
			.build()
		);
	}

	//판매자 회원정보 조회
	@Override
	public SellerMemberDetailResponseDto findSellerMember(
		SellerMemberDetailRequestDto sellerMemberDetailRequestDto) {
		Member member = memberRepository.findByUuid(sellerMemberDetailRequestDto.getUuid())
			.orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));

		boolean isSubscribed = false;
		if (sellerMemberDetailRequestDto.getUuid() != null) {
			if (sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
				sellerMemberDetailRequestDto.getUuid(), member.getUuid()).isEmpty()) {
				isSubscribed = false;
			} else if (sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
					sellerMemberDetailRequestDto.getUuid(), member.getUuid())
				.get().getState() == SubscribeState.SUBSCRIBE) {
				isSubscribed = true;
			} else if (sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
					sellerMemberDetailRequestDto.getUuid(), member.getUuid())
				.get().getState() == SubscribeState.UNSUBSCRIBE) {
				isSubscribed = false;

			}
		}

		return SellerMemberDetailResponseDto.builder()
			.name(member.getName())
			.profileImage(member.getProfileImage())
			.isSubscribed(isSubscribed)
			.build();
	}

	//회원 신고
	@Override
	@Transactional
	public void addReport(String uuid, MemberReportRequestDto memberReportRequestDto) {
		String reportedUuid = memberReportRequestDto.getReportedUuid();
		memberRepository.findByUuid(reportedUuid)
			.orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));
		userReportRepository.findByReporterUuidAndReportedUuid(uuid, reportedUuid)
			.ifPresent(report -> {
				throw new CustomException(ResponseStatus.DUPLICATE_REPORT);
			});
		UserReport userReport = UserReport.builder()
			.reporterUuid(uuid)
			.reportedUuid(reportedUuid)
			.reportReason(memberReportRequestDto.getReportReason())
			.processingResult("처리중")
			.build();

		userReportRepository.save(userReport);
	}
}
