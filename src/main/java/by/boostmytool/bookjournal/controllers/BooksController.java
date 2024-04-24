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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        return "redirect:/books";
    }
}
