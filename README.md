Avant de lancer, modifier la classe src/tools/Const.java :
+ PATH_TO_INVERTED_FILE_FROM_INDEXER = "~/InvertedFileFromMerger/InvertedFileFromIndexer/";
+ PATH_TO_INVERTED_FILE_FROM_MERGER = "~/InvertedFileFromMerger/";
+ WEIGHTFILETMP = "~/InvertedFileFromMerger/WeightFileTmp/";
+ INVERTEDFILETMP = "~/InvertedFileFromMerger/InvertedFileTmp/";
+ MAX_NUMBER_OF_FILE = ## NOMBRE DE DOCUMENTS QUE VOUS VOULEZ INDEXER ##;
==========================================================================================================================
Pour lancer l'indexation :
java -cp bin tools.Main
=========================================================================================================================
Pour lancer une recherche :
java -cp bin tools.GraphicalInterface
Une fois que l'interface est lancée, il suffit de taper sa recherche dans l'unique champs de saisie. Ce qui aura pour effet d'afficher les fichiers correspondants.
