package com.robo.service;

import com.robo.repository.ShopsRepo;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class ShopsService {

    @Autowired
    ShopsRepo shopsRepository;

//    @Transactional(readOnly=true)
//    public List<Shops> findAll() {
//        return shopsRepository.findAll();
//    }
//

}
