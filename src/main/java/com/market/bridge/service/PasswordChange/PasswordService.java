package com.market.bridge.service.PasswordChange;

public interface PasswordService {
    public void sendAuthToken(String gmail);
    public boolean validateAuthToken(String code);
    public void changePassword(String token, String newPass);
}
