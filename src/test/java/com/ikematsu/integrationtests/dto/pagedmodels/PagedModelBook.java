package com.ikematsu.integrationtests.dto.pagedmodels;

import java.util.List;

import com.ikematsu.integrationtests.dto.BookDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelBook {

    @XmlElement(name = "content")
    private List<BookDTO> content;

    public PagedModelBook() {}

    public List<BookDTO> getContent() {
        return content;
    }

    public void setContent(List<BookDTO> content) {
        this.content = content;
    }
}
