package tools;


/**
 * Classe contenant les diverses constantes utilisés dans le programme
 * @author sbazin10
 *
 */
public class Const {
	
	
	/*
	 * Path vers le dossier contenant les documents du corpus
	 */
	public static final String pathToCorpus = "/public/iri/projetIRI/corpus/";	
	
	/*
	 * Path vers le fichier des stopwords français	 
	 */
	public static final String pathToStopWordsFile = "/projet/iri/bvh/Tools/frenchST.txt";
	
	/*
	 * Path vers le dossier contenant les Inverted Files générées par l'Indexer
	 */
	public static final String pathToInvertedFileFromIndexer = "/projet/iri/bvh/InvertedFileFromIndexer/";
	
	/*
	 * Path vers le dossier contenant les Inverted Files générées par l'InvertedFileMerger, après fusion puis découpage donc
	 */
	public static final String pathToInvertedFileFromMerger = "/projet/iri/bvh/InvertedFileFromMerger/";
	
	/*
	 * Path vers le dossier contenant les fichiers .poids générés par l'Indexer
	 */
	public static final String pathToWeightFiles = "/projet/iri/bvh/WeightFile";
	
	
	/*
	 * String représentant les lettres apparaissant le plus couramment en tant que première lettre d'un mot en français.
	 * Utilisé lors du découpage du fichier inverse fusionné en plusieurs fichiers inverses, triés par ordre alphabétique
	 */
	public static final String lettres = "abcdefghijklmnopqrstuvwxyzéèêâî";
	
	/*
	 * String représentant les chiffres.
	 * Utilisé comme la constant "lettres"
	 */
	public static final String chiffres = "0123456789";
	

}
