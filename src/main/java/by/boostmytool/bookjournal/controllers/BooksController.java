package by.boostmytool.bookjournal.controllers;

import by.boostmytool.bookjournal.models.Book;
import by.boostmytool.bookjournal.models.BookDto;
import by.boostmytool.bookjournal.services.BooksRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BooksRepository repo;

    @GetMapping({"", "/"})
    public String showBooksList(Model model){
        List<Book> books = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("books", books);
        return "books/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model){
        BookDto bookDto = new BookDto();
        model.addAttribute("bookDto", bookDto);
        return "books/CreateBook";
    }

    @PostMapping("/create")
    public String showCreatePage(@Valid @ModelAttribute BookDto bookDto, BindingResult result){
        if(bookDto.getImageFile().isEmpty()){
            result.addError(new FieldError("bookDto", "imageFile", "The image file is required"));
        }
        if (result.hasErrors()){
            return "books/CreateBook";
        }
        //save image file

        MultipartFile image = bookDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try{
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectory(uploadPath);
            }
            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        Book book = new Book();
        book.setName(bookDto.getName());
        book.setAuthor(bookDto.getAuthor());
        book.setGenre(bookDto.getGenre());
        book.setPages(bookDto.getPages());
        book.setRating(bookDto.getRating());
        book.setAnnotation(bookDto.getAnnotation());
        book.setImageBook(storageFileName);

        repo.save(book);

        return "redirect:/books";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id){

        try{
            Book book = repo.findById(id).get();
            model.addAttribute("book", book);

            BookDto bookDto = new BookDto();
            bookDto.setName(book.getName());
            bookDto.setAuthor(book.getAuthor());
            bookDto.setGenre(book.getGenre());
            bookDto.setPages(book.getPages());
            bookDto.setRating(book.getRating());
            bookDto.setAnnotation(book.getAnnotation());

        } catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/books";
        }
        return "books/EditBook";
    }
}
