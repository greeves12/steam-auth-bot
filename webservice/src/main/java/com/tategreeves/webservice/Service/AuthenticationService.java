package com.tategreeves.webservice.Service;

import com.tategreeves.webservice.Model.Token;
import com.tategreeves.webservice.Repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository repository;

    /*
    * Name: getToken
    * @param token - Token object
    * @return - An updated Token object
    * */
    public Token getToken(Token token){
        Optional<Token> token1 = repository.findById(token.getDiscord_iD());

        if(token1.isPresent()){
            if(token1.get().isVerified()){
                Token val = token1.get();
                val.setToken("Already Registered");

                return val;
            }
            return token1.get();
        }

        String uuid = UUID.randomUUID().toString();
        token.setToken(uuid);
        return repository.save(token);
    }

    /*
    * Name: updatedVerification
    * @param token - Token object
    * @param status - verification flag
    * @return - an updated Token object
    * */
    public Token updateVerification(Token token, boolean status){
        Token existingToken = repository.findById(token.getDiscord_iD()).get();
        existingToken.setVerified(status);
        return repository.save(existingToken);
    }

    /*
    * Name: deletedToken
    * @param discord_id - Unique discord id
    * @return - Message that object has been removed from the database
    * */
    public void deleteToken(String discord_id){
        repository.deleteById(discord_id);
    }

    public Optional<Token> getByToken(String token){
        return repository.getByToken(token);
    }

    public List<Token> getVerifiedTokens(){
        List<Token> tokens = repository.findAll();
        List<Token> newTokens = new ArrayList<>();
        for(Token t : tokens){
            if(t.isVerified()){
                newTokens.add(t);
            }
        }
        return newTokens;
    }
}
