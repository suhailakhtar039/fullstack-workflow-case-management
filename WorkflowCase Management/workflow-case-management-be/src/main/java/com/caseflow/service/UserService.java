package com.caseflow.service;

import com.caseflow.dto.RegisterRequest;

public interface UserService {
    void register(RegisterRequest request) throws IllegalAccessException;
}
