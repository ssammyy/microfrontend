package org.drive.mov.filemov.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class Utilities {
    @Autowired
    Environment environment;

   public Boolean isActivated(){
       String year = environment.getProperty("configurations.matrix");
       Date expiryDate = new Date(Integer.parseInt(year), 4, 1);
       Date currentDate = new Date();

       if (currentDate.before(expiryDate))
           return true;
       return false;

   }

}
