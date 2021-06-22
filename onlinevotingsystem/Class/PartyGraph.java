package com.onlie.voting.onlinevotingsystem.Class;

public class PartyGraph {
    String partyName;
    int vote;

    public PartyGraph() {
    }

    public PartyGraph(String partyName, int vote) {
        this.partyName = partyName;
        this.vote = vote;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
