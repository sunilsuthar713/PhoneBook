package phonebook.addon;

import java.util.regex.*;

public class validation {
    //PhoneNo Validation
    public static boolean isPhoneNo(String phoneNo)
    {
        return phoneNo.length()>0 && phoneNo.length()<15 && Pattern.compile("^[0-9]+").matcher(phoneNo).matches();
    }
    
    //Name Validation
    public static boolean isName(String name)
    {
        return name.length()>0 && Pattern.compile("^[A-Z a-z \\s]+").matcher(name).matches();
    }
    
    //Email Validation
    public static boolean isEmail(String email)
    {
        return email.length()>0 && Pattern.compile("^[A-Z a-z 0-9 +_.-]+@([a-z]+)\\.com$").matcher(email).matches(); 
    }
}
