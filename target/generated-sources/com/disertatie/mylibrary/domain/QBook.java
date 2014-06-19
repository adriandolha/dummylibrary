package com.disertatie.mylibrary.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = 2146127771L;

    public static final QBook book = new QBook("book");

    public final SimplePath<Author> author = createSimple("author", Author.class);

    public final SimplePath<Category> category = createSimple("category", Category.class);

    public final DateTimePath<java.util.Calendar> dateAdded = createDateTime("dateAdded", java.util.Calendar.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isbn = createString("isbn");

    public final SimplePath<Language> language = createSimple("language", Language.class);

    public final StringPath picture = createString("picture");

    public final SimplePath<Publisher> publisher = createSimple("publisher", Publisher.class);

    public final StringPath title = createString("title");

    public final StringPath volume = createString("volume");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata<?> metadata) {
        super(Book.class, metadata);
    }

}

