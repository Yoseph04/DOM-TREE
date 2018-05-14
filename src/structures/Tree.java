package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */

	
		
	
	public void build() {
		/** COMPLETE THIS METHOD **/
		
		if(sc == null) {
			return;
		}
		String lineCurrent;
		int lengthOfCurrent;
		Stack<TagNode> htmlTags = new Stack<TagNode>();
		sc.nextLine();
		root = new TagNode("html",null,null);//set root to start at html
		htmlTags.push(root);//pushes tags into to root
		TagNode newHtml;
		TagNode htmlPtr;
		
		while(sc.hasNextLine()) {
			 lineCurrent = sc.nextLine(); 
			boolean isHtml = false;//sets isHtml to false
			if(lineCurrent.charAt(0) == '<') { //ending of tags
				if(lineCurrent.charAt(1) == '/') {
					htmlTags.pop();
					continue;
				} else {
					lineCurrent = lineCurrent.substring(1,lineCurrent.length()-1);
					isHtml = true;//this checks to see if it html
				}
			}
			 newHtml = new TagNode(lineCurrent, null, null);
			if(htmlTags.peek().firstChild == null) {//checks to if the firstchild is null
				htmlTags.peek().firstChild = newHtml; 
			} else {
				htmlPtr = htmlTags.peek().firstChild;
			//while sibling != null
				while(htmlPtr.sibling != null) {
					htmlPtr = htmlPtr.sibling;
				}
				htmlPtr.sibling = newHtml;
			}
			if( isHtml == true ){//checks to see if html is true
				htmlTags.push(newHtml);
			}
		}
}
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	//helper method:
		private void helperReplace(TagNode root, String oldHtml, String newTag) {
			
			if(root == null) {//base case
				return;
			}
			else if(root.tag.equals(oldHtml)) {//checks to see if the tag equals the old tag
				root.tag = newTag;
			}
		//Recursively go through the firstChild and Sibling 
			helperReplace(root.firstChild,oldHtml,newTag);
		//Sibling
			helperReplace(root.sibling,oldHtml,newTag);
			
		}
	//Method: 
	public void replaceTag(String oldTag, String newTag) {
		if(root == null) {
			return;
		}
		if(oldTag == null) {
			return;
		}
		if(newTag == null) {
			return;
		}
		
		//calls helper method
		helperReplace(root,oldTag,newTag);
	}
	
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	private void helperBold(int rowTable, int counter, TagNode prevTag, TagNode traverseTag) {//helper method:
		//checks to see if traverse is null:
		if(traverseTag == null) {
			return;
		}
		
		else if(traverseTag.tag.equals("tr")) {
			counter++;//increments counter
		}
		else if(counter == rowTable && traverseTag.firstChild == null) {
			prevTag.firstChild = new TagNode("b", traverseTag, null);
		}
		//recurivsely go through each row
		helperBold(rowTable, counter, traverseTag, traverseTag.firstChild);
		//goes to the sibling
		helperBold(rowTable, counter, traverseTag, traverseTag.sibling);
	}
	
	
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		
		if(root == null) {
			return;
		}
		//now using the helper method we can bold each table
		if(row <= 0) { // checks to see if row is less than 0
			return;
		}else {//otherwise call our helper method
			helperBold(row,0,root,root.firstChild);
		}
		
		
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	
//method:
	
	public void removeTag(String tag) { 
		if(root == null) {
			return;
		}
		
		 if(tag.equals("p") || tag.equals("em") || tag.equals("b")) { //checks to see if tag equals any these html
			tagRemoverOne(root,tag);
		}
		 else if(tag.equals("ol") || tag.equals("ul")) {//same as above
			tagRemoverTwo(root,tag);
		}
	}
	//helper method one
	public void tagRemoverOne(TagNode rootTemp, String strTarget) { //consist for p, em, b
		if(rootTemp == null || strTarget == null) { 
			return;
		}
		else {
		tagRemoverOne(rootTemp.firstChild, strTarget);
		if(rootTemp.sibling != null && rootTemp.sibling.tag.equals(strTarget)){
			TagNode traverse = rootTemp.firstChild;
			while(traverse.sibling != null){
				//pointer
				traverse = traverse.sibling;		
			}
			traverse.sibling = rootTemp.sibling.sibling;
			//root.sibling to sibling of firstChild
			rootTemp.sibling = rootTemp.sibling.firstChild;	
		}
		if(rootTemp.firstChild != null && rootTemp.firstChild.tag.equals(strTarget)){ // checks to see if firstChild and firstChildTag is not equal to null and string Target
			rootTemp.firstChild = rootTemp.firstChild.firstChild;
		}
		//recursion:
		tagRemoverOne(rootTemp.sibling, strTarget);
			
		}
	}
	//second helper method
	public void tagRemoverTwo(TagNode rootTemp, String strTarget) { // consist for ol and ul
		if( strTarget  == null ) {
			return;
		}
		
		if(rootTemp == null) {
			return;
		}
		//recursion:
		else {
		String p = " p";
		//recursion part
		tagRemoverTwo(rootTemp.firstChild, strTarget);
		//now if statement similar to first helper method:
		if(rootTemp.sibling != null && rootTemp.sibling.tag.equals(strTarget)) {
			
			TagNode traverse = rootTemp.sibling.firstChild;
			//while:
			while(traverse.sibling != null) {
				traverse.tag = "p"; // makes it equal to p
				traverse = traverse.sibling;
			}
			traverse.tag = "p"; // makes ptr  = "p"
			traverse.sibling = rootTemp.sibling.sibling;
			rootTemp.sibling = rootTemp.sibling.firstChild;
		}
		if(rootTemp.firstChild != null && rootTemp.firstChild.tag.equals(strTarget)) {
			//pointer:
			TagNode traverse = rootTemp.firstChild.firstChild;
			//while
			while(traverse.sibling != null) {
				traverse.tag = "p"; // makes ptr  = p
				traverse = traverse.sibling;
			}
			traverse.tag = "p"; // makes ptr  = p
			traverse.sibling = rootTemp.firstChild.sibling;
			rootTemp.firstChild = rootTemp.firstChild.firstChild;
		}
		//Recursion:
		String tag;
		tagRemoverTwo(rootTemp.sibling,strTarget);
		
	}
		
}

	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */

	//helper method:
	private void helperAdd(TagNode curr, String word, String tag) {
		//Base Case for method
		if(curr == null){
			return; 
		}
		
		//Recursion
		helperAdd(curr.firstChild, word, tag);
		helperAdd(curr.sibling, word, tag);
		
		if(curr.firstChild == null) {
			while(curr.tag.toLowerCase().contains(word)) {
				//splits the array accordingly
				String[] arr = curr.tag.split(" ");
				int found = 0;
				String stringTagged = ""; //tags the word
				StringBuilder wordTagger = new StringBuilder(curr.tag.length());//stringbuilder
				int strWords;
				for( strWords = 0; strWords < arr.length; strWords++) {
					if(arr[strWords].toLowerCase().matches(word+"[.,?!:;]?")) {//checks puncation
						found = 1; //true 
						stringTagged = arr[strWords];
						int i = strWords+1;
							while( i < arr.length) {
							wordTagger.append(arr[i]+" ");
							i++;
						}
						break;//breaks out of loop structure
					}
				}
				if(found == 0){//false -> return
					return;
				}
				
				String finalWord = wordTagger.toString().trim();//trims the final word
				if(strWords == 0) { //if stringWords = 0 then creates the tag to suround the word
					curr.firstChild = new TagNode(stringTagged, null, null);
					curr.tag = tag;
					if(!finalWord.equals("")) { 
						curr.sibling = new TagNode(finalWord, null, curr.sibling);
						curr = curr.sibling;
					}
				}//otherwise: 
				else {
					TagNode taggedWordNode = new TagNode(stringTagged, null, null);
					TagNode htmlNew = new TagNode(tag, taggedWordNode, curr.sibling);
					curr.sibling = htmlNew;
					curr.tag = curr.tag.replaceFirst(" " + stringTagged, "");
					if(!finalWord.equals("")) { // not equal to empty string
						curr.tag = curr.tag.replace(finalWord, "");
						htmlNew.sibling = new TagNode(finalWord, null, htmlNew.sibling);
						curr = htmlNew.sibling;
					}
				}
			} 
		}
	}
	
	
	public void addTag(String word, String tag) {
		if(word == null) { //make sure word is not null
			return;
		}
		if(root == null) {
			return;
		}
		if(tag.equals("em") || tag.equals("b")) {
			helperAdd(root, word.toLowerCase(), tag);
		}
	}
	


	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}