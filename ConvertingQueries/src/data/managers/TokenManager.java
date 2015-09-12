package data.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import data.objects.FromStatement;
import data.objects.SelectStatement;
import data.objects.Statement;
import data.objects.TableNameStatement;
import databases.OracleDatabase;
import databases.PostgreSQLDatabase;
import databases.SQLServerDatabase;

public class TokenManager {

	private String fromDatabase;
	private String toDatabase;
	
	private List<Statement> queryListToken;
	private List<Statement> listOfStatements;

	public TokenManager(String fromDatabase, String toDatabase){
		this.fromDatabase = fromDatabase;
		this.toDatabase = toDatabase;
		this.queryListToken = new ArrayList<Statement>();
		this.listOfStatements = new ArrayList<Statement>();
	}

	public List<Statement> parse(String query){
		StringTokenizer tokens = new StringTokenizer(query);
		makeListOfStatements(tokens);
		System.out.println(listOfStatements);
		// TODO make parse
		return queryListToken;
	}

	private void makeListOfStatements(StringTokenizer tokens)
	{
		while(tokens.hasMoreTokens()){
			String token = tokens.nextToken();
			if(token.equals(TokenStatement.SELECT_STATEMENT)){
				token = tokens.nextToken();
				while(!token.equals(TokenStatement.FROM_STATEMENT)){
					listOfStatements.add(new TableNameStatement(token));
					token = tokens.nextToken();
				}
				SelectStatement selectStatement = new SelectStatement();
				selectStatement.setColumns(listOfStatements);
				queryListToken.add(selectStatement);
				listOfStatements = new ArrayList<Statement>();
			}
			
			if(token.equals(TokenStatement.FROM_STATEMENT)){
				token = tokens.nextToken();
				while(!token.equals(TokenStatement.WHERE_STATEMENT)){
					listOfStatements.add(new TableNameStatement(token));
					token = tokens.nextToken();
				}
				FromStatement fromStatement = new FromStatement();
				fromStatement.setTables(listOfStatements);
				listOfStatements = new ArrayList<Statement>();
			}
			if(token.equals(TokenStatement.WHERE_STATEMENT)){
				if(!token.equals(TokenStatement.AND_STATEMENT) || !token.equals(TokenStatement.OR_STATEMENT)){
					// UNDONE
				}
			}
		}
		
	}

	public void analyseStatement(StringTokenizer tokens, String token)
	{
		if(token.equals(TokenStatement.SELECT_STATEMENT)){

			token = tokens.nextToken();
			while(!token.equals(TokenStatement.FROM_STATEMENT)){
				listOfStatements.add(new TableNameStatement(token));
				token = tokens.nextToken();
			}
			SelectStatement selectStatement = new SelectStatement();
			selectStatement.setColumns(listOfStatements);
			queryListToken.add(selectStatement);
			listOfStatements = new ArrayList<Statement>();
		}
	}

	public void validate(String query)
	{
		if(toDatabase.equals(TokenDatabaseNames.SQL_SERVER))
		{
			SQLServerDatabase sql = new SQLServerDatabase();
			sql.parseSQLServer(fromDatabase, query);
		}
		else if(toDatabase.equals(TokenDatabaseNames.ORACLE))
		{
			OracleDatabase sql = new OracleDatabase();
			sql.parseOracle(fromDatabase, query);
		}
		else if(toDatabase.equals(TokenDatabaseNames.POSTGRESQL))
		{
			PostgreSQLDatabase sql = new PostgreSQLDatabase();
			sql.parsePostgreSQL(fromDatabase, query);
		}
	}

}
