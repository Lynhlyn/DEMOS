package com.example.baskend.SanPham.QuanLySanPham.controller;

import com.example.baskend.SanPham.QuanLySanPham.entity.ChatLieu;
import com.example.baskend.SanPham.QuanLySanPham.repository.ChatLieuRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chat-lieu")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ChatLieuController {
    @Autowired
    ChatLieuRepo chatLieuRepo;

    @GetMapping
    public List<ChatLieu> getAllChatLieu() {
        return chatLieuRepo.findAll();
    }
}
