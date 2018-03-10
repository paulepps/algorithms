var N = 0;		// number of data points entered 
var maxN = 20;	// maximum number of data points possible
var M = 4; 		// number of independent variables 
var X = new makeArray2(M, maxN);		
var Y = new Array;
var SX = 0;
var SY = 0;
var SXX = 0;
var SXY = 0;
var SYY = 0;
var m = 0; 		// degree of polymonial regression
var abort = false;

var regrCoeff = new Array;
var sigDig = 4;	// accuracy 

function makeArray2 (X,Y)
	{
	var count;
	this.length = X+1;
	for (var count = 0; count <= X+1; count++)
		// to allow starting at 1
		this[count] = new makeArray(Y);
	} // makeArray2

function makeArray (Y)
	{
	var count;
	this.length = Y+1;
	for (var count = 0; count <= Y+1; count++)
		this[count] = 0;
	} // makeArray

function det(A)
	{
	var Length = A.length-1;
		// formal length of a matrix is one bigger
	if (Length == 1) return (A[1][1]);
	else
		{
		var i;
		var sum = 0;
		var factor = 1;
		for (var i = 1; i <= Length; i++)
			{
			if (A[1][i] != 0)
				{
				// create the minor
				minor = new makeArray2(Length-1,Length-1);
				var m;
				var n;
				var theColumn;
				for (var m = 1; m <= Length-1; m++) // columns
					{
					if (m < i) theColumn = m;
					else theColumn = m+1;
					for (var n = 1; n <= Length-1; n++)
						{
						minor[n][m] = A[n+1][theColumn];
						} // n
					} // m
				// compute its determinant
				sum = sum + A[1][i]*factor*det(minor);
				}
			factor = -factor;	// alternating sum
			} // end i
		} // recursion
	return(sum);
	} // end determinant

function inverse(A) {
	var Length = A.length - 1;
	var B = new makeArray2(Length, Length);  // inverse
	var d = det(A);
	if (d == 0) alert("singular matrix--check data");
	else
		{
		var i;
		var j;
		for (var i = 1; i <= Length; i++)
			{
			for (var j = 1; j <= Length; j++)
				{
				// create the minor
				minor = new makeArray2(Length-1,Length-1);
				var m;
				var n;
				var theColumn;
				var theRow;
				for (var m = 1; m <= Length-1; m++) // columns
					{
					if (m < j) theColumn = m;
					else theColumn = m+1;
					for (var n = 1; n <= Length-1; n++)
						{
						if (n < i) theRow = n;
						else theRow = n+1;
						minor[n][m] = A[theRow][theColumn];
						} // n
					} // m
				// inverse entry
				var temp = (i+j)/2;
				if (temp == Math.round(temp)) factor = 1;
				else factor = -1;
				
				B[j][i] =  det(minor)*factor/d; 

				
				} // j
			
			} // end i
		} // recursion
	return(B);
	} // end inverse

function shiftRight(theNumber, k) {
	if (k == 0) return (theNumber)
	else
		{
		var k2 = 1;
		var num = k;
		if (num < 0) num = -num;
		for (var i = 1; i <= num; i++)
			{
			k2 = k2*10
			}
		}
	if (k>0) 
		{return(k2*theNumber)}
	else 
		{return(theNumber/k2)}
	}

function roundSigDig(theNumber, numDigits) {
	with (Math)
		{
		if (theNumber == 0) return(0);
		else if(abs(theNumber) < 0.000000000001) return(0);
		// warning: ignores numbers less than 10^(-12)
		else
			{
			var k = floor(log(abs(theNumber))/log(10))-numDigits
			var k2 = shiftRight(round(shiftRight(abs(theNumber),-k)),k)
			if (theNumber > 0) return(k2);
			else return(-k2)
			} // end else
		}
	}

function clearForms (){
	document.theForm.output.value=""
	for (i = 0; i <= 19; i++)
		{
		document.theForm[3+6*i].value=""
		document.theForm[4+6*i].value=""
		document.theForm[5+6*i].value="" 
		document.theForm[6+6*i].value=""
		document.theForm[7+6*i].value=""
		document.theForm[8+6*i].value=""
		}
	}

function stripSpaces (InString)  {
	OutString="";
	for (Count=0; Count < InString.length; Count++)  {
		TempChar=InString.substring (Count, Count+1);
		if (TempChar!=" ")
			OutString=OutString+TempChar;
		}
	return (OutString);
	}

function buildxy()  {
	e = 2.718281828459045;
	pi = 3.141592653589793;	
	abort = false;
	with (Math)
		{
		N = 0; 		// number of data points
		var searching = true;
		var numvariables = 4;
		if (document.theForm[4+6*1].value == "") numvariables = 1;
		else if (document.theForm[5+6*1].value == "") numvariables = 2;
		else if (document.theForm[6+6*1].value == "") numvariables = 3;
		else numvariables = 4;
		
		for (var i = 0; i <= 19; i++)			// arrays start at 0
			{
		
			theString1 = stripSpaces(document.theForm[3+6*i].value);	
			if (theString1 == "") searching = false;
			theString2 = stripSpaces(document.theForm[4+6*i].value);	
			if ( (numvariables >= 2) && (theString2 == "") ) searching = false;
			theString3 = stripSpaces(document.theForm[5+6*i].value);	
			if ( (numvariables >= 3) && (theString3 == "") ) searching = false;
			theString4 = stripSpaces(document.theForm[6+6*i].value);	
			if ( (numvariables >= 4) && (theString4 == "") ) searching = false;
			
			if ( (searching) && (!abort) )
				{ 
				N++;
				X[1][N] = eval(theString1);
				if (numvariables >= 2) X[2][N] = eval(theString2); 
				if (numvariables >= 3) X[3][N] = eval(theString3); 
				if (numvariables >= 4) X[4][N] = eval(theString4);
				theString = stripSpaces(document.theForm[7+6*i].value);
				if (theString == "") {alert("You have not entered a y-value for data point number "+N); abort = true}
				else Y[N] = eval(theString);
				}
	
			} // of i = 1 to 15
		} // end of with math
	M = numvariables;		// the numer of active variables
	if (!abort)
		{
		if (N == 0) {alert("Enter data first"); abort = true} 
		else if (N < M+1) {alert("You have entered too few data points"); abort = true}
		} // if !abort
	}

function linregr()
	{
	if (!abort) {
		e = 2.718281828459045;
		pi = 3.141592653589793;
		var k;
		var i;
		var j;
		var sum;
		
		B = new makeArray(M+1);
		P = new makeArray2(M+1, M+1);
		invP = new makeArray2(M+1, M+1);
		var mtemp = M+1;
		with (Math)
			{
			// First define the matrices B and P
			for (i = 1; i <=  N; i++) X[0][i] = 1;
			for (i = 1; i <= M+1; i++)
				{
				sum = 0;
				for (k = 1; k <= N; k++) sum = sum + X[i-1][k]*Y[k];
				B[i] = sum;
				
				for (j = 1; j <= M+1; j++) 
					{ 
					sum = 0;
					for (k = 1; k <= N; k++) sum = sum + X[i-1][k]*X[j-1][k];
					P[i][j] = sum;
					}
				} // i

			invP = inverse(P);	
			for (k = 0; k <= M; k++)
				{
				sum = 0;
	
				for (j = 1; j <= M+1; j++)
					{
					sum = sum + invP[k+1][j]*B[j];
					} // j 
				regrCoeff[k] = sum;
				} // k
			} // end of with math
		} // end of if not abort
	}

function calc(){
	
	with (Math)
		{
		buildxy();
		linregr();
		var output = "y = " + roundSigDig(regrCoeff[0], sigDig);
		for (i = 1; i <=M; i++)
			output += " + " + roundSigDig(regrCoeff[i], sigDig) + "x" + i ;
		console.log(output);
		} // end of with math
}
