package kr.or.ddit.vo;

import java.security.Principal;

//누군가 로그인 했을때 동작해야함
public class MemberVOWrapper implements Principal{ //어뎁터
	private MemberVO realMember; //어뎁티

	//기본 생성자 사라짐
	public MemberVOWrapper(MemberVO realMember) {
		super();
		this.realMember = realMember;
	}
	
	public MemberVO getRealMember() {
		return realMember;
	}

	@Override
	public String getName() {
		return realMember.getMemId();
	}
	
}
