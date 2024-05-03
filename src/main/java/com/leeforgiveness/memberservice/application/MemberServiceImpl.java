package com.leeforgiveness.memberservice.application;

import com.leeforgiveness.memberservice.domain.Member;
import com.leeforgiveness.memberservice.dto.request.MemberRequestDto;
import com.leeforgiveness.memberservice.dto.response.MemberDetailResponseDto;
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
            .build();
    }

    //회원정보 수정
    @Override
    @Transactional
    public void updateMember(String memberUuid,
        MemberRequestDto memberResumeInfoRequestDto) {
        Member member = memberRepository.findByUuid(memberUuid)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        memberRepository.save(Member.builder()
            .uuid(member.getUuid())
            .email(member.getEmail())
            .name(member.getName())
            .phoneNum(member.getPhoneNum())
            .resumeInfo(memberResumeInfoRequestDto.getResumeInfo())
            .handle(member.getHandle())
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
            .build()
        );
    }
}
