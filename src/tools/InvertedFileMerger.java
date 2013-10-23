package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * 
 * @author sbazin10
 *	Fusionne les fichiers inverses générés par l'indexer.
 */
public class InvertedFileMerger {

	private File directorySource;
	private String invertedFilePathSource;
	private int marqueur = 1;
	
	private String lettres = "abcdefghijklmnopqrstuvwxyzéèêàâîë";
	private String chiffres ="0123456789";
	
	
	/**
	 * 
	 * @param pathToDirectorySource
	 * 		Path du repertoire contenant les inverted files générés par l'indexer.
	 **/
	public InvertedFileMerger(String pathToDirectorySource){
		invertedFilePathSource = pathToDirectorySource;
		
		directorySource = new File(pathToDirectorySource);		
	}
	
	
	/** 
	 * La méthode à lancer, qui démarre le merge
	 * @throws IOException
	 */
	public void run() throws IOException{	
		
		String[] fileNames = directorySource.list();		
		this.merge(fileNames);		
	}
	
	
	
	/**
	 * Fusionne récursivement deux a deux tous les fichiers d'un repertoire, supprime les fichiers sources, et relance la fusion sur les fichiers resultats
	 * Quand il ne reste plus que un seul fichier, le découpe
	 * @param fileNames
	 * 		FileName du répertoire d'ou on va merger les fichiers
	 * @throws IOException
	 */
	private void merge(String[] fileNames) throws IOException{
		
		//S'il n'ya qu'un seul fichier dans le repertoire, on lui applique un traitement pour le découper, par exemple
		//alphabétiquement ou alors comme on veut.
		if(fileNames.length == 1){
			//cette méthode peut etre interchangée pour créer des resultats différents
			splitInvertedFileResult(fileNames[0]);	
			
			File file = new File(fileNames[0]);
			file.delete();
		}
		else{
			for(int i=0;i<fileNames.length;i=i+2){	
				if(i>=fileNames.length-1){
				}
				else{
					File fileA = new File(invertedFilePathSource+fileNames[i]);
					File fileB = new File(invertedFilePathSource+fileNames[i+1]);
					File fileResultat = new File(invertedFilePathSource+"FileResultat"+marqueur+".txt");
					
					mergeInvertedFiles(fileA, fileB, fileResultat);
					
					fileA.delete();
					fileB.delete();
					
					marqueur++;
				}
			}					
			String[] newFileNames = directorySource.list();			
			merge(newFileNames);
		}		
	}
	
	/**
	 * Split un fichier inverse en 26 fichiers inverse, un par lettres de l'alphabet (+ 1 fichier pour les chiffres et 1 fichier pour
	 * les caractères bizarres)
	 * @param fileName
	 * @throws IOException
	 */
	private void splitInvertedFileResult(String fileName) throws IOException{
		File file = new File(invertedFilePathSource+fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter trucDeMerde = new BufferedWriter(new FileWriter(invertedFilePathSource+"trucDeMerde.txt"));
		BufferedWriter chiffre = new BufferedWriter(new FileWriter(invertedFilePathSource+"chiffre.txt"));
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(invertedFilePathSource+"a.txt"));
		String currentString = "a";
		
		
		String mot = reader.readLine();
		while(mot !=null){
			String firstLetter = mot.substring(0,1).toLowerCase();
			if(lettres.contains(firstLetter)){
				if(currentString.equals(firstLetter)){
					writer.write(mot+"\n");					
				}
				else{
					writer.close();
					currentString = firstLetter;
					writer = new BufferedWriter(new FileWriter(invertedFilePathSource+currentString+".txt"));
					writer.write(mot+"\n");	
				}				
			}
			else if(chiffres.contains(firstLetter)){
				chiffre.write(mot+"\n");
			}
			else{
				trucDeMerde.write(mot+"\n");
			}
			
			mot=reader.readLine();
		}
		
		reader.close();
		trucDeMerde.close();
		chiffre.close();
		writer.close();
	}
	
	/**
	 * Découpe un fichier inverse en de nombreux fichiers inverses, ordonnées par les deux premières lettres des mots qu'ils contiennent
	 * @param fileName
	 * @throws IOException
	 */
	private void splitInvertedFileResultWithTwoLetters(String fileName) throws IOException{
		File file = new File(invertedFilePathSource+fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter trucDeMerde = new BufferedWriter(new FileWriter(invertedFilePathSource+"trucDeMerde.txt"));
		BufferedWriter chiffre = new BufferedWriter(new FileWriter(invertedFilePathSource+"chiffre.txt"));
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(invertedFilePathSource+"a.txt"));
		String currentString = "aa";
		
		
		String mot = reader.readLine();
		while(mot !=null){
			String firstLetter = mot.substring(0,1).toLowerCase();
			String secondLetter = mot.substring(1,2).toLowerCase();
			String twoFirstLetters = mot.substring(0,2).toLowerCase();
			
			if(lettres.contains(firstLetter)){
				
				if(currentString.equals(twoFirstLetters)){
					writer.write(mot+"\n");					
				}
				else{
					if(lettres.contains(secondLetter)){
						
						writer.close();
						currentString = twoFirstLetters;
						writer = new BufferedWriter(new FileWriter(invertedFilePathSource+currentString+".txt"));
						writer.write(mot+"\n");	
					}
					else{
						trucDeMerde.write(mot+"\n");					
					}
				}				
			}
			else if(chiffres.contains(firstLetter)){
				chiffre.write(mot+"\n");
			}
			else{
				trucDeMerde.write(mot+"\n");
			}			
			mot=reader.readLine();
		}
		
		reader.close();
		trucDeMerde.close();
		chiffre.close();
		writer.close();
	}
	
	
	/**
	 * 
	 * @param invertedFile1 
	 * 		Premier fichier inverse à fusionner
	 * @param invertedFile2
	 * 		Second fichier inverse à fusionner
	 * @param mergedInvertedFile
	 * 		Fichier inverse resultat de la fusion des deux
	 * @throws IOException
	 */
	private void mergeInvertedFiles(File invertedFile1, File invertedFile2,
			File mergedInvertedFile) throws IOException{
		BufferedReader readerA = new BufferedReader(new FileReader(invertedFile1));
		BufferedReader readerB = new BufferedReader(new FileReader(invertedFile2));
		BufferedWriter writer = new BufferedWriter(new FileWriter(mergedInvertedFile));

		try{
			String motA = readerA.readLine();
			String motB = readerB.readLine();

			while(motA !=null && motB !=null){
				//On split les lignes courantes selon la tabulation
				String[] wordFreqDocsA = motA.split("\t");
				String[] wordFreqDocsB = motB.split("\t");
				if(wordFreqDocsA[0].equals(wordFreqDocsB[0])){
					String[] documentListA = wordFreqDocsA[2].split(",");
					String[] documentListB = wordFreqDocsB[2].split(",");

					TreeSet<String> documentListResult = new TreeSet<String>();
					for(String s : documentListB){
						documentListResult.add(s);					
					}
					for(String s : documentListA){
						documentListResult.add(s);					
					}

					int a = Integer.parseInt(wordFreqDocsA[1]) + Integer.parseInt(wordFreqDocsB[1]);
					writer.write(wordFreqDocsA[0]+"\t"+a+"\t");
					
					//Ecriture de la liste des documents
					Iterator it = documentListResult.iterator();
					int size = documentListResult.size();
					int cpt=0;
					while (it.hasNext()) {
						cpt++;
						writer.append((String) it.next());
						if (cpt != size) {
							writer.append(",");
						}
					}
					writer.append('\n');
					
					motA = readerA.readLine();
					motB = readerB.readLine();
				}
				else if(wordFreqDocsA[0].compareTo(wordFreqDocsB[0])<0){
					writer.write(motA+"\n");
					motA = readerA.readLine();							
				}
				else if(wordFreqDocsA[0].compareTo(wordFreqDocsB[0])>0){
					writer.write(motB+"\n");
					motB = readerB.readLine();							
				}					
			}

			//Si le document A est vide on Ã©crit la fin du document B
			if(motA == null){
				while(motB !=null){
					writer.write(motB+"\n");
					motB = readerB.readLine();					
				}			
			}	

			//Si le document B est vide on Ã©crit la fin du document A
			if(motB == null){
				while(motA !=null){
					writer.write(motA+"\n");
					motA = readerA.readLine();					
				}			
			}	
		}finally{
			writer.close();
			readerA.close();
			readerB.close();
		}
	}
	
	
}
