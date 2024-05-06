package com.leeforgiveness.memberservice.application;

import com.leeforgiveness.memberservice.domain.Member;
import com.leeforgiveness.memberservice.domain.SnsInfo;
import com.leeforgiveness.memberservice.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.infrastructure.MemberRepository;
import com.leeforgiveness.memberservice.infrastructure.SnsInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SnsInfoRepository snsInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //이메일 중복 확인
    @Override
    public void duplicationEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
    }

    //SNS 회원 추가
    @Override
    @Transactional
    public void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto) {
        if (snsInfoRepository.findBySnsIdAndSnsType(snsMemberAddRequestDto.getSnsId(), snsMemberAddRequestDto.getSnsType()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }

        duplicationEmail(snsMemberAddRequestDto.getEmail());

        String uuid = UUID.randomUUID().toString();

        String character = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder handle = new StringBuilder("@user-");
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            handle.append(character.charAt(random.nextInt(character.length())));
        }

        Member member = Member.builder()
                .email(snsMemberAddRequestDto.getEmail())
                .name(snsMemberAddRequestDto.getName())
                .phoneNum(snsMemberAddRequestDto.getPhoneNum())
                .uuid(uuid)
                .handle(handle.toString())
                .build();

        memberRepository.save(member);

        SnsInfo snsInfo = SnsInfo.builder()
                .snsId(snsMemberAddRequestDto.getSnsId())
                .snsType(snsMemberAddRequestDto.getSnsType())
                .member(member)
                .build();

        snsInfoRepository.save(snsInfo);
    }

    //토큰 생성
    private String createToken(Member member) {
        return jwtTokenProvider.generateToken(member);
    }

    //소셜 로그인
    @Override
    @Transactional
    public TokenResponseDto snsLogin(SnsMemberLoginRequestDto snsMemberLoginRequestDto) {
        SnsInfo snsInfo = snsInfoRepository.findBySnsIdAndSnsType(snsMemberLoginRequestDto.getSnsId(), snsMemberLoginRequestDto.getSnsType())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Member member = memberRepository.findById(snsInfo.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        if (member.isTerminationStatus()) {
            throw new IllegalArgumentException("탈퇴한 회원입니다.");
        }

        String token = createToken(member);

        return TokenResponseDto.builder()
                .token(token)
                .build();
    }

    //회원정보 조회
    @Override
    public MemberDetailResponseDto findMember(String uuid) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        return MemberDetailResponseDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phoneNum(member.getPhoneNum())
                .resumeInfo(member.getResumeInfo())
                .handle(member.getHandle())
                .profileImage(member.getProfileImage())
                .build();
    }

    //회원정보 수정
    @Override
    @Transactional
    public void updateMember(String memberUuid,
                             MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = memberRepository.findByUuid(memberUuid)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        memberRepository.save(Member.builder()
                .uuid(member.getUuid())
                .email(member.getEmail())
                .name(memberUpdateRequestDto.getName())
                .phoneNum(memberUpdateRequestDto.getPhoneNum())
                .resumeInfo(memberUpdateRequestDto.getResumeInfo())
                .handle(memberUpdateRequestDto.getHandle())
                .profileImage(memberUpdateRequestDto.getProfileImage())
                .build()
        );
    }

    //회원 탈퇴
    @Override
    @Transactional
    public void removeMember(String uuid) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        memberRepository.save(Member.builder()
                .uuid(member.getUuid())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNum(member.getPhoneNum())
                .resumeInfo(member.getResumeInfo())
                .handle(member.getHandle())
                .terminationStatus(true)
                .profileImage(member.getProfileImage())
                .build()
        );
    }
}
