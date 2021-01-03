Feature: Match name with matrilineality
    Match a cow name with their family name

    Scenario Outline: Uppee
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname      | answer |
            | Uppee        | Uppee  |
            | uppee        | Uppee  |
            | suppee       |        |
            | uppee av bon | Uppee  |
            | polled uppee | Uppee  |

    Scenario Outline: Europe
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname       | answer |
            | Europe        | Europe |
            | europe        | Europe |
            | Europe av bon | Europe |
            | polled Europe | Europe |
            | aaEurope      |        |
            | Europeer      |        |

    Scenario Outline: Eure
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname     | answer |
            | Eure        | Eure   |
            | eure        | Eure   |
            | Eure av bon | Eure   |
            | polled Eure | Eure   |
            | aaEure      |        |
            | Eurer       |        |

    Scenario Outline: Eglantine
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname          | answer    |
            | Eglantine        | Eglantine |
            | eglantine        | Eglantine |
            | Eglantine av bon | Eglantine |
            | polled Eglantine | Eglantine |
            | aaEglantine      |           |
            | Eglantiner       |           |
            | Jessika          | Eglantine |
            | jessika          | Eglantine |
            | Jessika av bon   | Eglantine |
            | polled Jessika   | Eglantine |
            | aaJessika        |           |
            | Jessikabbb       |           |
            | Jesika           | Eglantine |
            | jesika           | Eglantine |
            | Jesika av bon    | Eglantine |

    Scenario Outline: Etincelle
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname          | answer    |
            | Etincelle        | Etincelle |
            | etincelle        | Etincelle |
            | Etincelle av bon | Etincelle |
            | polled Etincelle | Etincelle |
            | aaEtincelle      |           |
            | Etincellebbb     |           |
            | Madame           | Etincelle |
            | madame           | Etincelle |
            | Madame av bon    | Etincelle |
            | polled Madame    | Etincelle |
            | aaMadame         |           |
            | Madamebbb        |           |

    Scenario Outline: Epargne
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname        | answer  |
            | Epargne        | Epargne |
            | epargne        | Epargne |
            | Epargne av bon | Epargne |
            | polled Epargne | Epargne |
            | aaEpargne      |         |
            | Epargnebbb     |         |
            | Evita          | Epargne |
            | evita          | Epargne |
            | Evita av bon   | Epargne |
            | polled Evita   | Epargne |
            | aaEvita        |         |
            | Evitabbb       |         |

    Scenario Outline: Fanny
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname      | answer |
            | Fanny        | Fanny  |
            | fanny        | Fanny  |
            | Fanny av bon | Fanny  |
            | polled Fanny | Fanny  |
            | aaFanny      |        |
            | Fannybbb     |        |

    Scenario Outline: Esther
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname        | answer |
            | Esther         | Esther |
            | esther         | Esther |
            | Esther av bon  | Esther |
            | polled Esther  | Esther |
            | aaEsther       |        |
            | Estherbbb      |        |
            | Minerve        | Esther |
            | minerve        | Esther |
            | Minerve av bon | Esther |
            | polled Minerve | Esther |
            | aaMinerve      |        |
            | Minervebbb     |        |
            | Minerv         | Esther |
            | minerv         | Esther |
            | Minerv av bon  | Esther |
            | polled Minerv  | Esther |

    Scenario Outline: Estafette
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname          | answer    |
            | Estafette        | Estafette |
            | estafette        | Estafette |
            | Estafette av bon | Estafette |
            | polled Estafette | Estafette |
            | aaEstafette      |           |
            | Estafettebbb     |           |
            | Elle             | Estafette |
            | elle             | Estafette |
            | Elle av bon      | Estafette |
            | polled Elle      | Estafette |
            | aaElle           |           |
            | Ellebbb          |           |

    Scenario Outline: Elyssee
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname        | answer  |
            | Elyssee        | Elyssee |
            | elyssee        | Elyssee |
            | Elyssee av bon | Elyssee |
            | polled Elyssee | Elyssee |
            | aaElyssee      |         |
            | Elysseebbb     |         |
            | Elysee         | Elyssee |
            | elysee         | Elyssee |

    Scenario Outline: Delphine
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname         | answer   |
            | Delphine        | Delphine |
            | delphine        | Delphine |
            | Delphine av bon | Delphine |
            | polled Delphine | Delphine |
            | aaDelphine      |          |
            | Delphinebbb     |          |
            | Escall          | Delphine |
            | escall          | Delphine |
            | Escall av bon   | Delphine |
            | polled Escall   | Delphine |
            | aaEscall        |          |
            | Escallbbb       |          |

    Scenario Outline: Klara
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname      | answer |
            | Klara        | Klara  |
            | klara        | Klara  |
            | Klara av bon | Klara  |
            | polled Klara | Klara  |
            | aaKlara      |        |
            | Klarabbb     |        |
            | Klary        | Klara  |
            | klary        | Klara  |
            | Klary av bon | Klara  |
            | polled Klary | Klara  |

    Scenario Outline: Henny
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname        | answer  |
            | Hellene        | Hellene |
            | hellene        | Hellene |
            | Hellene av bon | Hellene |
            | polled Hellene | Hellene |
            | aaHellene      |         |
            | Hellenebbb     |         |
            | Henny          | Hellene |
            | henny          | Hellene |
            | Henny av bon   | Hellene |
            | polled Henny   | Hellene |
            | aaHenny        |         |
            | Hennybbb       |         |

    Scenario Outline: Flute
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname      | answer |
            | Flute        | Flute  |
            | flute        | Flute  |
            | Flute av bon | Flute  |
            | polled Flute | Flute  |
            | aaFlute      |        |
            | Flutebbb     |        |
            | Ceris        | Flute  |
            | ceris        | Flute  |
            | Ceris av bon | Flute  |
            | polled Ceris | Flute  |
            | aaCeris      |        |
            | Cerisbbb     |        |

    Scenario Outline: Etoile
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname       | answer |
            | Etoile        | Etoile |
            | etoile        | Etoile |
            | Etoile av bon | Etoile |
            | polled Etoile | Etoile |
            | aaEtoile      |        |
            | Etoilebbb     |        |
            | Mimell        | Etoile |
            | mimell        | Etoile |
            | Mimell av bon | Etoile |
            | polled Mimell | Etoile |
            | aaMimell      |        |
            | Mimellbbb     |        |

    Scenario Outline: Palette
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname        | answer  |
            | Palette        | Palette |
            | palette        | Palette |
            | Palette av bon | Palette |
            | polled Palette | Palette |
            | aaPalette      |         |
            | Palettebbb     |         |

    Scenario Outline: Gamine
        Given matrilinealities are setup
        When I test the cow name "<cowname>"
        Then I should get matrilineality name "<answer>"

        Examples:
            | cowname       | answer |
            | Gamine        | Gamine |
            | gamine        | Gamine |
            | Gamine av bon | Gamine |
            | polled Gamine | Gamine |
            | aaGamine      |        |
            | Gaminebbb     |        |
            | Nelly         | Gamine |
            | nelly         | Gamine |
            | Nelly av bon  | Gamine |
            | polled Nelly  | Gamine |
            | aaNelly       |        |
            | Nellybbb      |        |

