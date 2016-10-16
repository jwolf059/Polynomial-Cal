/**
 * Homework 3
 * 
 * TCSS 342 - Winter 2016
 */
package model;

import model.LinkedList.Iterator;

/**
 * Creates a Polynomial Object that uses a Collection to store Literal object that will contain int
 * values for a Coefficient and an Exponent. The Collection will represent a polynomial allowing
 * for mathematical operation to be conducted on them. Supported operations include: add, minus, times
 * and derivative. The add, minus, and times methods do the math operations between two polynomials. 
 * The derivative is only taken once and is done on a single polynomial. 
 * 
 * @author Jeremy Wolf
 * @version 20 January 2016
 *
 */

public class Polynomial {

	/*
	 * Stores the Polynomial Values.
	 */
	private LinkedList myTerms;

	/*
	 * Constructs a new Polynomial. The Polynomial is represent by a LinkedList
	 * each node holds a Coefficient value and an Exponent value.
	 */
	public Polynomial() {
		myTerms = new LinkedList();
	}
	
	/**
	 * Inserts new monomial values in to the Polynomial. Puts all values in descending order
	 * in regards to their exponential power. If two monomials share the same exponent then 
	 * they are added together. 
	 * @param Coefficient int value representing the Coefficient of the Monomial.
	 * @param theExponent int value representing the Exponent of the Monomial
	 */
	public void insertTerm(int theCoefficient, int theExponent) {
		
		Iterator itr = myTerms.zeroth();
		if (myTerms.isEmpty()) {
			Literal monomial = new Literal(theCoefficient, theExponent);
			myTerms.insert(monomial, itr);
		} else {
			while (itr.hasNext()) {
				Literal temp = null;
				
				// Will use the next nodes Element to evaluate for insertion position.
				// this allows for the iterator to be pointed at the desired insertion point.
				if (itr.getNode().getNext() != null) {
					temp = (Literal)itr.getNode().getNext().getElement();
				} else {
					
					// If the iterator is pointed at the last node then we use the current nodes
					// Element for insertion details. This allows a check for null on line 55.
					temp = (Literal)itr.getElement();
				}
				int tempExponent = temp.getExponent();
				if (tempExponent != theExponent) {
					
						// if the Exponent is larger then the new element is inserted at the next spot.
						// the evaluation is done on the iterators next position (not Current).
						if (theExponent > tempExponent) {
							Literal monomial = new Literal(theCoefficient, theExponent);
							myTerms.insert(monomial, itr);
							break;
						
						// Checks to determine if the current node is the last, meaning the current
						// element has the smallest exponent.
							
						} else if (itr.getNode().getNext() == null){
							Literal monomial = new Literal(theCoefficient, theExponent);
							myTerms.insert(monomial, itr);
							break;
						}
						
				// if the Exponents are the same the coefficients are added.		
				} else {     
					temp.setCoefficient(temp.getCoefficient() + theCoefficient);
					if (temp.getCoefficient() == 0) {
						myTerms.remove(itr);
					}
					break;
					}
				itr.next();
			}	
		}	
	}

	/**
	 * Zeros the Polynomial out. 
	 */
	public void zeroPolynomial() {
		myTerms.makeEmpty();
	}
	
	/**
	 * Negates all Coefficient values in the Polynomial. 
	 * @return a new Polynomial with negated coefficients.
	 */
	public Polynomial negate() {
		Polynomial negateCopy = new Polynomial();
		Iterator itrNegate = myTerms.iterator(); 
		while (itrNegate.hasNext()) {
			Literal negateTemp = (Literal)itrNegate.getElement();
			int negateCoeff = negateTemp.getCoefficient() *-1;
			int exponent = negateTemp.getExponent();
			negateCopy.insertTerm(negateCoeff, exponent);
			itrNegate.next();
			}
		return negateCopy; 
	}
	
	/**
	 * Adds the passed polynomial to this polynomial. This method
	 * calls the private helper method calculate(Polynomial, int).
	 * @param thePolynomial the Polynomial to be added to this Polynomial.
	 * @return a new Polynomial with the Sum's.
	 */
	public Polynomial plus(Polynomial thePolynomial) {
		return calculate(thePolynomial, 1);
	}
	
	/**
	 * Subtracts the passed polynomial with this polynomial. This method
	 * calls the private helper method calculate(Polynomial, int).
	 * @param thePolynomial the Polynomial to be subtracted from this Polynomial.
	 * @return a new Polynomial with the difference.
	 */
	public Polynomial minus(Polynomial thePolynomial) {
		return calculate(thePolynomial, 2);
	}
	
	/**
	 * Multiplies the passed polynomial with this polynomial. This method relies on the 
	 * insert method for adding monomials together with the same exponent. 
	 * @param thePolynomial the Polynomial to be multiplied by this Polynomial.
	 * @return a new Polynomial with the products of this operation.
	 */
	public Polynomial times(Polynomial thePolynomial) {
		Polynomial product = new Polynomial();
		LinkedList other = thePolynomial.toLinkedList();
		Iterator itrA = myTerms.iterator();
		// Iterates through myTerms multiplying the Coefficients together and adding
		// the Exponents. Each set of operations are added to the new Polynomial.
		while (itrA.hasNext()) {
			Literal aLit = (Literal)itrA.getElement();
			int aLitCoeff = aLit.getCoefficient();
			int aLitExpo = aLit.getExponent();
			Iterator itrB = other.iterator();
			while (itrB.hasNext()) {
				Literal bLit = (Literal)itrB.getElement();
				int bLitCoeff = bLit.getCoefficient();
				int bLitExpo = bLit.getExponent();
				product.insertTerm(aLitCoeff * bLitCoeff, aLitExpo + bLitExpo);
				itrB.next();
			}
			itrA.next();
		}
		return product;
	}
	
	/**
	 * Calculates the Derivative of the Polynomial.
	 * @return a new Polynomial with the derivative of this Polynomial.
	 */
	public Polynomial derivative() {
		Polynomial deriv = new Polynomial();
		Iterator derivItr = myTerms.iterator();
		while (derivItr.hasNext()) {
			Literal divLit = (Literal)derivItr.getElement();
			int divCoefficient = divLit.getCoefficient();
			int divExponent = divLit.getExponent();
			if (divExponent != 0) {
				deriv.insertTerm(divCoefficient * divExponent, divExponent - 1);
			}
			derivItr.next();
		}
		return deriv;
	}
	
	/**
	 * Creates a String representation of the Polynomial for the visualization of the 
	 * equation.
	 * @return a String representation of the Polynomial. 
	 */
	public String print() {
		Boolean firstOne = true;
		StringBuilder polySB = new StringBuilder();
		Iterator itr2 = myTerms.iterator();
		
		// Displays a zero when the polynomial is empty.
		 if (!itr2.hasNext()) {
			 polySB.append("0");
		 } else {
			 
			 while (itr2.hasNext()) {
				 
				 Literal temp = (Literal)itr2.getNode().getElement();
				 int coefficient = temp.getCoefficient();
				 int exponent = temp.getExponent();
				 // StringBuilder values when the Coefficient is less than Zero.
				 if (coefficient < 0) {
					 polySB.append(coefficient);
					 
					 // StringBuilder values when the Exponent is less than Zero.
					 if (exponent < 0) {
						 polySB.append("x^(" + exponent + ")");
					// StringBuilder values when the Exponent is greater than Zero. 
					 } else if (exponent > 1) {
						 polySB.append("x^" + exponent);
					// StringBuilder values when the Exponent is equals to one.
					 } else if (exponent == 1){
						 polySB.append("x");
					 } 
					 
				 // StringBuilder values when the Coefficient is greater than One.
				 } else if (coefficient > 1) {
					 // Fence post for when the first term is positive. 
					 if (firstOne) {
						 polySB.append(coefficient);
					// If the value is the second or further value this appends a + before it.	 
					 } else {
						 polySB.append("+" + coefficient);
					 }
					// StringBuilder values when the Exponent is less than Zero. 
					 if (exponent < 0) {
						 polySB.append("x^(" + exponent + ")");
					// StringBuilder values when the Exponent is greater than One.
					 } else if (exponent > 1) {
						 polySB.append("x^" + exponent);
					// StringBuilder values when the Exponent is equals to one.
					 } else if (exponent == 1){
						 polySB.append("x");
					 } 
					 
				// StringBuilder values when the Coefficient is equal to one.	 
				 } else if (coefficient == 1) {
					 // It this is not the first positive value in the polynomial a + is appended.
					 if (!firstOne) {
						 polySB.append("+");
					 } 
					// StringBuilder values when the Exponent is less than Zero.
					 if (exponent < 0) {
						 polySB.append("x^(" + exponent + ")");
					// StringBuilder values when the Exponent is greater than Zero.
					 } else if (exponent > 1) {
						 polySB.append("x^" + exponent);
					 } else if (exponent == 1){
						 polySB.append("x");
					 } else {
						 polySB.append(coefficient);
					 }
				 }
				 firstOne = false;
				 itr2.next();	
			 }
		 }
		 return polySB.toString();
	}
	/**
	 * Creates a copy of the Polynomial list. 
	 * @return a deep copy of the Polynomial list.
	 */
	
	public LinkedList toLinkedList() {
		LinkedList copyToReturn = new LinkedList();
		Iterator itrCopy = copyToReturn.zeroth(); 
		Iterator itrOrg = myTerms.iterator();
		while (itrOrg.hasNext()) {
			Literal copyTemp = (Literal)itrOrg.getElement();
			int coefficient = copyTemp.getCoefficient();
			int exponent = copyTemp.getExponent();
			Literal copy = new Literal(coefficient, exponent);
			copyToReturn.insert(copy, itrCopy);
			itrCopy.next();
			itrOrg.next();
			}
		return copyToReturn;
		
	}
	
	/**
	 * Private helper method for the plus and minus methods.
	 * @param thePolynomial the polynomial used for calculation.
	 * @param theCalc int value for adding or subtracting.
	 * @return a new Polynomial with the calculations completed.
	 */
	private Polynomial calculate(Polynomial thePolynomial, int theCalc) {
		Polynomial sum = new Polynomial();
		LinkedList other = thePolynomial.toLinkedList();
		Iterator itrA = myTerms.iterator();
		Iterator itrB = other.iterator();
		// Will iterate through both Polynomials until the end of one is reached.
		while (itrA.hasNext() && itrB.hasNext()) {
			Literal aLit = (Literal)itrA.getElement();
			int aExponent = aLit.getExponent();
			Literal bLit = (Literal)itrB.getElement();
			int bExponent = bLit.getExponent();
			
			// When both exponents are the same we can just add or subtract the Coefficients.
			// After inserting we step to the next value in BOTH polynomials.
			if (aLit.getExponent() == bLit.getExponent()) {
				// Adding Coefficients.
				if (theCalc == 1) {
					if (aLit.getCoefficient() + bLit.getCoefficient() != 0) {
						sum.insertTerm(aLit.getCoefficient() + bLit.getCoefficient(), aExponent);
					}
					itrA.next();
					itrB.next();
				// Subtracting Coefficients.	
				} else if (theCalc == 2) {
					if (aLit.getCoefficient() - bLit.getCoefficient() != 0) {
						sum.insertTerm(aLit.getCoefficient() - bLit.getCoefficient(), aExponent);
					}
					itrA.next();
					itrB.next();
				}
			// When the exponent from "this" polynomial is greater  we just insert it in 
			// to the new Polynomial. Then step to the next value in "this" polynomial.
			} else if (aLit.getExponent() > bLit.getExponent()) {
				sum.insertTerm(aLit.getCoefficient(), aExponent);
				itrA.next();
			// When the exponent from theOther polynomial is greater we insert it in to
			// the new Polynomial. Then step to the next value in theOther polynomial. 
			} else if (bExponent > aExponent) {
				// Add a positive coefficient to the new Polynomial. (Addition).
				if (theCalc == 1) {
					sum.insertTerm(bLit.getCoefficient(), bExponent);
					itrB.next();
			   // Insert the coefficient after it has been negated. (Subtraction)
				} else if (theCalc == 2) {
					sum.insertTerm(bLit.getCoefficient() * -1, bExponent);
					itrB.next();
				}
			}
		}
		// Will iterate over the last values in this Polynomial.
		if (itrA.hasNext()) {
			while (itrA.hasNext()) {
				Literal elementA = (Literal)itrA.getElement();
				sum.insertTerm(elementA.getCoefficient(), elementA.getExponent());
				itrA.next();
			}
		// Will iterate over the last values in theOther Polynomial..
		} else 	if (itrB.hasNext()) {
			while (itrB.hasNext()) {
				if (theCalc == 1) {
					Literal elementB = (Literal)itrB.getElement();
					sum.insertTerm(elementB.getCoefficient(), elementB.getExponent());
					itrB.next();
				} else if (theCalc == 2) {
					Literal elementB = (Literal)itrB.getElement();
					sum.insertTerm(elementB.getCoefficient() * -1, elementB.getExponent());
					itrB.next();
				}
			}
		}
		return sum;
	}
}
