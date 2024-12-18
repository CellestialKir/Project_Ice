package org.example.project_ice.services;

import org.example.project_ice.entity.History;
import org.example.project_ice.repository.HistoryREpository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryREpository histRepo;

    public List<History> findAll(){
        return histRepo.findAll();
    }

    public List<History> findBySearch(String search) throws ParseException {
        List<History> histories = histRepo.findAll();
        histories.clear();
        try{
            histories.addAll(histRepo.findByDate(LocalDate.parse(search)));
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            histories.addAll(histRepo.findByNumberOfProducts(Integer.parseInt(search)));
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            histories.addAll(histRepo.findByTotal(Integer.parseInt(search)));
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            histories.addAll(histRepo.findByUser_UsernameContaining(search));
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            histories.addAll(histRepo.findByProduct_NameContaining(search));
        }catch (Exception e){
            System.out.println(e);
        }

        return histories;
    }

}
