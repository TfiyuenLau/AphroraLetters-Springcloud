export interface LibraryAuthor {
    id: number;
    characterName: string;
    picUrl: string;
    introduction: string;
    isEffective: number;
    authorIndices: number[];
}

export interface AuthorIndex {
    articleId: number;
    title: string;
    pdfUrl: string;
    authorId: number;
    createBy: string;
    isEffective: boolean;
    libraryAuthor: LibraryAuthor | string;
}
