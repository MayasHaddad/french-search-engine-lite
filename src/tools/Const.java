package tools;

/**
 * Classe contenant les diverses constantes utilisés dans le programme
 * 
 * @author sbazin10
 * 
 */
public class Const {

	/*
	 * Path vers le dossier contenant les documents du corpus
	 */
	public static final String PATH_TO_CORPUS = "/public/iri/projetIRI/corpus/";

	/*
	 * Path vers le dossier contenant les documents du corpus
	 */
	// public static final String PATH_TO_LITTLE_CORPUS =
	// "/public/iri/projetIRI/corpus/0000/";
	public static final String PATH_TO_LITTLE_CORPUS = "/public/iri/projetIRI/corpus/0000";

	/*
	 * Path vers le fichier des stopwords français
	 */
	public static String PATH_TO_STOP_WORDS = "/projet/iri/bvh/Tools/frenchST.txt";

	/*
	 * Path vers le fichier des stopwords français
	 */
	public static String PATH_TO_NBFILES = "/projet/iri/bvh/Tools/nbFiles.txt";

	/*
	 * Path vers le dossier contenant les Inverted Files générées par l'Indexer
	 */
	public static final String PATH_TO_INVERTED_FILE_FROM_INDEXER = "/projet/iri/bvh/InvertedFileFromMerger1000000/InvertedFileFromIndexer/";
	// "/projet/iri/bvh/InvertedFileFromIndexer/"

	/*
	 * Path vers le dossier contenant les Inverted Files générées par
	 * l'InvertedFileMerger, après fusion puis découpage donc
	 */
	public static final String PATH_TO_INVERTED_FILE_FROM_MERGER = "/projet/iri/bvh/InvertedFileFromMerger1000000/";
	// "/projet/iri/bvh/InvertedFileFromMerger"

	/*
	 * Path vers le dossier contenant les fichiers .poids générés par l'Indexer
	 */
	public static final String PATH_TO_WEIGHT_FILES = "/projet/iri/bvh/WeightFile/";

	public static final String PATH_TO_DOC_FREQ = "/projet/iri/bvh/DocFreq";

	/*
	 * String représentant les lettres apparaissant le plus couramment en tant
	 * que première lettre d'un mot en français. Utilisé lors du découpage du
	 * fichier inverse fusionné en plusieurs fichiers inverses, triés par ordre
	 * alphabétique
	 */
	public static final String LETTRES = "abcdefghijklmnopqrstuvwxyzéèêâî";

	/*
	 * String représentant les chiffres. Utilisé comme la constant "lettres"
	 */
	public static final String CHIFFRES = "0123456789";

	public static final String LETTRES_ET_CHIFFRES = "abcdefghijklmnopqrstuvwxyzéèêâî0123456789";

	// /////////////// INDEXATION

	// Remove the too simple words
	public static boolean REMOVE_STOP_WORDS = false;

	// Number of files in the corpus
	public static Integer NB_FILES_IN_CORPUS = null;

	// the used normalizer
	public final static Normalizer NORMALIZER = new FrenchStemmer();

	// the type of file considered
	public static String EXTENTION_KEEP = ".txt";

	public static String WEIGHTFILETMP = "/projet/iri/bvh/InvertedFileFromMerger1000000/WeightFileTmp/";
	// "/projet/iri/bvh/WeightFileTmp/"

	public static String INVERTEDFILETMP = "/projet/iri/bvh/InvertedFileFromMerger1000000/InvertedFileTmp/";
	// /projet/iri/bvh/InvertedFileTmp/

	public static final Integer MAX_NUMBER_OF_FILE = 1000000;
	public static Integer CURRENT_NUMBER_OF_FILE = 0;

}
