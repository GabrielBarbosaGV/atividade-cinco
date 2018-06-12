package br.ufpe.cin.if688.minijava.visitor;

import br.ufpe.cin.if688.minijava.ast.And;
import br.ufpe.cin.if688.minijava.ast.ArrayAssign;
import br.ufpe.cin.if688.minijava.ast.ArrayLength;
import br.ufpe.cin.if688.minijava.ast.ArrayLookup;
import br.ufpe.cin.if688.minijava.ast.Assign;
import br.ufpe.cin.if688.minijava.ast.Block;
import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.Call;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.False;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.Identifier;
import br.ufpe.cin.if688.minijava.ast.IdentifierExp;
import br.ufpe.cin.if688.minijava.ast.IdentifierType;
import br.ufpe.cin.if688.minijava.ast.If;
import br.ufpe.cin.if688.minijava.ast.IntArrayType;
import br.ufpe.cin.if688.minijava.ast.IntegerLiteral;
import br.ufpe.cin.if688.minijava.ast.IntegerType;
import br.ufpe.cin.if688.minijava.ast.LessThan;
import br.ufpe.cin.if688.minijava.ast.MainClass;
import br.ufpe.cin.if688.minijava.ast.MethodDecl;
import br.ufpe.cin.if688.minijava.ast.Minus;
import br.ufpe.cin.if688.minijava.ast.NewArray;
import br.ufpe.cin.if688.minijava.ast.NewObject;
import br.ufpe.cin.if688.minijava.ast.Not;
import br.ufpe.cin.if688.minijava.ast.Plus;
import br.ufpe.cin.if688.minijava.ast.Print;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.This;
import br.ufpe.cin.if688.minijava.ast.Times;
import br.ufpe.cin.if688.minijava.ast.True;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.While;
import br.ufpe.cin.if688.minijava.symboltable.SymbolTable;
import java.util.Enumeration;

public class TypeCheckVisitor implements IVisitor<Type> {

	private SymbolTable symbolTable;
  private Class currClass;
  private Method currMethod;

	TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
    currClass = null;
    currMethod = null;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		n.i1.accept(this);
		n.i2.accept(this);
		n.s.accept(this);
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
    currClass = symbolTable.getClass(n.i.toString());

		n.i.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
    currClass = symbolTable.getClass(n.i.toString());

		n.i.accept(this);
		n.j.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
    currMethod =
      symbolTable.getMethod(n.i.toString(), currClass.getId());

		n.t.accept(this);
		n.i.accept(this);
		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		n.e.accept(this);
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	public Type visit(IntArrayType n) {
		return n;
	}

	public Type visit(BooleanType n) {
		return n;
	}

	public Type visit(IntegerType n) {
		return n;
	}

	// String s;
	public Type visit(IdentifierType n) {
		return n;
	}

	// StatementList sl;
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
	  if (!(n.e.accept(this) instanceof BooleanType)) {
      // TODO Correct Exit
    }
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Type visit(While n) {
		if (!(n.e.accept(this) instanceof BooleanType)) {
      // TODO Correct Exit
    }
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
    Type assigneeType =
      symbolTable.getVarType(currMethod, currClass, n.i.toString());

		n.i.accept(this);

    Type assignedType = n.e.accept(this);

    if (!(assigneeType.getClass() == assignedType.getClass())) {
      // TODO Correct Exit
    }

		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
    Type assigneeType =
      symbolTable.getVarType(currMethod, currClass, n.i.toString());
    
		n.i.accept(this);

		Type assignedType = n.e1.accept(this);

		Type assignedSize = n.e2.accept(this);

    if (!((assignedSize instanceof IntegerType) && (assigneeType.getClass() == assignedType.getClass()))) {
      // TODO Correct Exit
    }

		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type boolONe = n.e1.accept(this);
		Type boolTwo = n.e2.accept(this);

    if (!((boolOne instanceof BooleanType) && (boolTwo instanceof BooleanType))) {
      // TODO Correct Exit
    }

		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type integerOne = n.e1.accept(this);
		Type integerTwo = n.e2.accept(this);

    if (!((integerOne instanceof IntegerType) && (integerTwo instanceof IntegerType))) {
      // TODO Correct Exit
    }

		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		Type integerOne = n.e1.accept(this);
		Type integerTwo = n.e2.accept(this);

    if (!((integerOne instanceof IntegerType) && (integerTwo instanceof IntegerType))) {
      // TODO Correct Exit
    }

		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type integerOne = n.e1.accept(this);
		Type integerTwo = n.e2.accept(this);

    if (!((integerOne instanceof IntegerType) && (integerTwo instanceof IntegerType))) {
      // TODO Correct Exit
    }

		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		Type integerOne = n.e1.accept(this);
		Type integerTwo = n.e2.accept(this);

    if(!((integerOne instanceof IntegerType) && (integerTwo instanceof IntegerType))) {
      // TODO Correct Exit
    }

		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type intArray = n.e1.accept(this);
		Type integerType = n.e2.accept(this);


		return null;
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		Type integerType = n.e.accept(this);

    if (!(integerType instanceof IntegerType)) {
      // TODO Correct Exit
    }

		return new IntegerType();
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		Type classIdentifier = n.e.accept(this);
		Type methodIdentifier = n.i.accept(this);

    // Check if expression resolves to class
    if (!((classIdentifier instanceof IdentifierType))) {
      // TODO Correct Exit
    }

    String className = ((IdentifierType) classIdentifier).s;

    // Check if class exists
    if (!(symbolTable.containsClass(className))) {
      // TODO Correct Exit
    }

    Class methodClass = symbolTable.getClass(className);

    String methodName = ((IdentifierType) methodIdentifier).s;

    // Check if method is contained by resolved class
    if (!(methodClass.containsMethod(methodName))) {
      // TODO Correct Exit
    }

    Method method = methodClass.getMethod(methodName);

    Enumeration params = method.getParams();

		for (int i = 0; i < n.el.size(); i++) {
      if (!(params.hasMoreElements())) {
        // TODO Correct Exit
      }

			Type passedParamType = n.el.elementAt(i).accept(this);
      Type actualParamType = params.nextElement();

      if (!((passedParamType.getClass()) == (actualParamType.getClass()))) {
        // TODO Correct Exit
      }
		}

		return method.type();
	}

	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}

	public Type visit(False n) {
		return new BooleanType();
	}

	// String s;
	public Type visit(IdentifierExp n) {
    String name = n.s;

    Type foundType = null;

    if (currClass.containsVar(name)) {
      foundType = currClass.getVar(name).type();
    } else if (currMethod.containsVar(name)) {
      foundType = currMethod.getVar(name).type();
    } else {
      // TODO Correct Exit
    }

		return foundType;
	}

	public Type visit(This n) {
		return IdentifierType(currClass.getId());
	}

	// Exp e;
	public Type visit(NewArray n) {
		Type integerType = n.e.accept(this);

    if (!(integerType instanceof IntegerType)) {
      // TODO Correct Exit
    }

		return new IntArrayType();
	}

	// Identifier i;
	public Type visit(NewObject n) {
    if (!(symbolTable.containsClass(n.id))) { 
      // TODO Correct Exit
    }

		return symbolTable.getClass(n.id).type();
	}

	// Exp e;
	public Type visit(Not n) {
		Type booleanType = n.e.accept(this);

    if (!(booleanType instanceof BooleanType) {
      // TODO Correct Exit
    }

		return new BooleanType();
	}

	// String s;
	public Type visit(Identifier n) {
    Type foundType = null;

    if (currClass.containsVar(n.s)) {
      foundType = currClass.getVar(n.s).type();
    } else if (currMethod.containsVar(n.s)) {
      foundType = currMethod.getVar(n.s).type();
    } else {
      // TODO Correct Exit
    }

		return foundType;
	}
}
