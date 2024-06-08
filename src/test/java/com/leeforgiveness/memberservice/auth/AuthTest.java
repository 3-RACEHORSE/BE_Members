//package com.leeforgiveness.memberservice.auth;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.leeforgiveness.memberservice.auth.application.MemberService;
//import com.leeforgiveness.memberservice.auth.application.MemberServiceImpl;
//import com.leeforgiveness.memberservice.auth.domain.Member;
//import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailRequestDto;
//import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailResponseDto;
//import com.leeforgiveness.memberservice.auth.infrastructure.MemberRepository;
//import com.leeforgiveness.memberservice.auth.infrastructure.SnsInfoRepository;
//import com.leeforgiveness.memberservice.auth.infrastructure.UserReportRepository;
//import com.leeforgiveness.memberservice.common.security.JwtTokenProvider;
//import com.leeforgiveness.memberservice.subscribe.domain.SellerSubscription;
//import com.leeforgiveness.memberservice.subscribe.infrastructure.SellerSubscriptionRepository;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//public class AuthTest {
//
//	private MemberRepository memberRepository = Mockito.mock(
//		MemberRepository.class);
//	private SnsInfoRepository snsInfoRepository = Mockito.mock(
//		SnsInfoRepository.class);
//	private JwtTokenProvider jwtTokenProvider = Mockito.mock(
//		JwtTokenProvider.class);
//	private UserReportRepository userReportRepository = Mockito.mock(
//		UserReportRepository.class);
//	private SellerSubscriptionRepository sellerSubscriptionRepository = Mockito.mock(
//		SellerSubscriptionRepository.class);
//	private MemberService memberService;
//
//	private String uuid;
//
//	@BeforeEach
//	public void setUp() {
//		memberService = new MemberServiceImpl(memberRepository, snsInfoRepository,
//			jwtTokenProvider, userReportRepository, sellerSubscriptionRepository);
//
//		uuid = "uuid";
//	}
//
//	@Test
//	@DisplayName("판매자 정보 조회 테스트")
//	public void findSellerMemberTest() {
//		// given
//		String expectedHandle = "expectedHandle";
//		String expectedUuid = "expectedUuid";
//		Member expectedMember = Member.builder()
//			.uuid(expectedUuid)
//			.build();
//
//		SellerSubscription sellerSubscription = SellerSubscription.builder()
//			.subscriberUuid(uuid)
//			.sellerUuid(expectedUuid)
//			.build();
//
//		Mockito.when(memberRepository.findByHandle(expectedHandle))
//			.thenReturn(Optional.of(expectedMember));
//
//		Mockito.when(
//				sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(uuid, expectedUuid))
//			.thenReturn(Optional.of(sellerSubscription));
//
//		SellerMemberDetailRequestDto sellerMemberDetailRequestDto = SellerMemberDetailRequestDto.builder()
//			.handle(expectedHandle).uuid("").build();
//		// when
//		SellerMemberDetailResponseDto result = memberService.findSellerMember(
//			sellerMemberDetailRequestDto);
//
//		// then
//		assertFalse(result.isSubscribed());
//	}
//}