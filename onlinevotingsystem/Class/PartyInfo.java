package com.onlie.voting.onlinevotingsystem.Class;

public class PartyInfo {
    String partyname;
    int totalVote;

    public PartyInfo() {
    }

    public PartyInfo(String partyname, int totalVote) {
        this.partyname = partyname;
        this.totalVote = totalVote;
    }

    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public int getTotalVote() {
        return totalVote;
    }

    public void setTotalVote(int totalVote) {
        this.totalVote = totalVote;
    }
}
