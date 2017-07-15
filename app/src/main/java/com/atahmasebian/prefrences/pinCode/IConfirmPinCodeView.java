package com.atahmasebian.prefrences.pinCode;


interface IConfirmPinCodeView {

    void showLoginwithPinCodeUi();
    void showConfirmPinCodeUi();
    String getConfirmPinCode();
    void setShownConfirmLayout(boolean isConfirmLayoutShown);

}
