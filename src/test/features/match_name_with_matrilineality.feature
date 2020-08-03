Feature: Match name with matrilineality
  Match a cow name with their family name

  Scenario Outline: Uppee
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Uppee         	| Uppee   	|
    | uppee         	| Uppee   	|
    | suppee 			|    		|
    | uppee av bon      | Uppee   	|
    | polled uppee      | Uppee   	|
    
  Scenario Outline: Europe
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Europe         	| Europe   	|
    | europe         	| Europe   	|
    | Europe av bon    	| Europe   	|
    | polled Europe     | Europe   	|
    | aaEurope         	| 		   	|
    | Europeer       	| 		   	|
    
  Scenario Outline: Eure
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Eure         		| Eure   	|
    | eure	         	| Eure   	|
    | Eure av bon    	| Eure   	|
    | polled Eure     	| Eure   	|
    | aaEure         	| 		   	|
    | Eurer       		| 		   	|
    
  Scenario Outline: Eglantine
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|    
    | Eglantine         | Eglantine |    
    | eglantine         | Eglantine |
    | Eglantine av bon  | Eglantine |
    | polled Eglantine  | Eglantine |
    | aaEglantine       | 		   	|
    | Eglantiner       	| 		   	|
    | Jessika     		| Eglantine |
    | jessika     		| Eglantine |
    | Jessika av bon  	| Eglantine |
    | polled Jessika  	| Eglantine |
    | aaJessika       	| 		   	|
    | Jessikabbb      	| 		   	|
    
  Scenario Outline: Etincelle
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Etincelle         | Etincelle |
    | etincelle	        | Etincelle |
    | Etincelle av bon  | Etincelle |
    | polled Etincelle  | Etincelle |
    | aaEtincelle       | 		   	|
    | Etincellebbb      | 		   	|
    
  Scenario Outline: Epargne
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Epargne         	| Epargne 	|
    | epargne	      	| Epargne 	|
    | Epargne av bon  	| Epargne 	|
    | polled Epargne  	| Epargne 	|
    | aaEpargne       	| 		   	|
    | Epargnebbb      	| 		   	|
    
  Scenario Outline: Fanny
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Fanny         	| Fanny   	|
    | fanny	        	| Fanny 	|
    | Fanny av bon  	| Fanny 	|
    | polled Fanny  	| Fanny 	|
    | aaFanny       	| 		   	|
    | Fannybbb      	| 		   	|
    
  Scenario Outline: Esther
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Esther         	| Esther   	|
    | esther	        | Esther 	|
    | Esther av bon  	| Esther 	|
    | polled Esther  	| Esther 	|
    | aaEsther       	| 		   	|
    | Estherbbb      	| 		   	|
    
  Scenario Outline: Estafette
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Estafette         | Estafette |
    | estafette	        | Estafette |
    | Estafette av bon  | Estafette |
    | polled Estafette  | Estafette |
    | aaEstafette       | 		   	|
    | Estafettebbb      | 		   	|
    
  Scenario Outline: Elyssee
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Elyssee         	| Elyssee 	|
    | elyssee	        | Elyssee 	|
    | Elyssee av bon  	| Elyssee 	|
    | polled Elyssee  	| Elyssee 	|
    | aaElyssee       	| 		   	|
    | Elysseebbb      	| 		   	|
    
  Scenario Outline: Delphine
    Given matrilinealities are setup
    When I test the cow name "<cowname>"
    Then I should get matrilineality name "<answer>"

  Examples:
    | cowname           | answer 	|
    | Delphine         	| Delphine 	|
    | delphine	        | Delphine 	|
    | Delphine av bon  	| Delphine 	|
    | polled Delphine  	| Delphine 	|
    | aaDelphine       	| 		   	|
    | Delphinebbb      	| 		   	|
    