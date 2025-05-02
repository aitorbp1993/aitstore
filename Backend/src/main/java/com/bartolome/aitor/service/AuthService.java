package com.bartolome.aitor.service;

import com.bartolome.aitor.dto.AuthResponse;
import com.bartolome.aitor.dto.UserRegisterDTO;

public interface AuthService {
    AuthResponse register(UserRegisterDTO request);
}
