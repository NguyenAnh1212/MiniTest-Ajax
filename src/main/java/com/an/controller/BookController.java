package com.an.controller;

import com.an.model.Book;
import com.an.model.Category;
import com.an.service.book.BookService;
import com.an.service.book.IBookService;
import com.an.service.category.CategoryService;
import com.an.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    @ModelAttribute("categories")
    private Iterable<Category> listCategories(){
        return categoryService.findAll();
    }


    @GetMapping("/list")
    public ModelAndView showList(){
        ModelAndView modelAndView = new ModelAndView("ajaxList");
        modelAndView.addObject("books",bookService.findAll());
        modelAndView.addObject("categories",categoryService.findAll());
        return modelAndView;
    }
    @GetMapping
    public ResponseEntity<Iterable<Book>> findAll(){
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id){
        Optional<Book> book = bookService.findById(id);
        System.out.println(book);
        if(!book.isPresent()){
            return new ResponseEntity<>(book.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        bookService.save(book);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Book> editBook(@PathVariable Long id,@RequestBody Book book){
        Optional<Book> bookOptional = bookService.findById(id);
        book.setId(bookOptional.get().getId());
        if (!bookOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.save(book);
        return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
//        book.setId(id);
//        bookService.save(book);
//        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id){
        bookService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
