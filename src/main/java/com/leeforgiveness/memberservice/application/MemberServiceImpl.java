package com.leeforgiveness.memberservice.application;

import com.leeforgiveness.memberservice.domain.Member;
import com.leeforgiveness.memberservice.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

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
