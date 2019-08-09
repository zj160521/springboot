package com.gm.domain.gamerecord;

import java.time.LocalDate;

public class MemberGameRecordAnalyze {
	private String memberId ;
	private Long profitAmount ;
	private Long betAmount ;
	private LocalDate date ;
	public String getMemberId() {
		return memberId;
	}
	public Long getProfitAmount() {
		return profitAmount;
	}
	public Long getBetAmount() {
		return betAmount;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setProfitAmount(Long profitAmount) {
		this.profitAmount = profitAmount;
	}
	public void setBetAmount(Long betAmount) {
		this.betAmount = betAmount;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
