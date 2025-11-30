/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projprodutos;

/**
 * @author rayssa
 */
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import javax.swing.JOptionPane;



public class ControleProduto {
    Connection conexao;
    PreparedStatement sql;
    Statement comando;
    ResultSet lista;
    
    Produto prod = new Produto();
    /*CRIANDO OBJ*/
    
    public ControleProduto() {
       Conectar();
    }
    
    public void Conectar(){
      try{
        String senha="";
        String usuario="root";
        String nome_banco="estoque_produtos";
        String servidor="localhost:3306";
        String driverName = "com.mysql.cj.jdbc.Driver";
        Class.forName(driverName);
        String url = "jdbc:mysql://" + servidor + "/" + nome_banco+"?useTimezone=true&serverTimezone=UTC"; 
        conexao = DriverManager.getConnection(url,usuario,senha);
        comando = conexao.createStatement();
        }
      
      catch(ClassNotFoundException e){
          JOptionPane.showMessageDialog(null,"Erro de Driver");
      }
      catch (Exception e){
           JOptionPane.showMessageDialog(null,"Erro no mysql");
      }
    }
    
    public void Cadastrar(String nome, int quant,double valor){
   
        prod.setNome(nome);
        prod.setQuant(quant);
        prod.setValor(valor);
        try {
            //                              Insert into produtos ( Nome ,Quant , Valor) values (1010 ,"Mouse" , 20 , 10);
            sql = conexao.prepareStatement("Insert into produtos(Nome ,Quant , Valor) values (?,?,?)");
           
            sql.setString(1, nome);
            sql.setInt(2,quant);
            sql.setDouble(3,valor);
            
            int verifica = sql.executeUpdate();
            if (verifica > 0){
                JOptionPane.showMessageDialog(null , "Cadastro Realizado com Sucesso !!");
            }
            else{
                JOptionPane.showMessageDialog(null , "Cadastro não realizado!!");
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"ERRO no mysql cadastar !!!"); 
        }
    
    }
    
    public Produto Consultar(int id){
        Produto p = null;
        try{
            sql = conexao.prepareStatement("SELECT * FROM produtos WHERE ID ="+id);
            lista = sql.executeQuery();
            
            if (lista.next()){
                p = new Produto();
                p.setId(lista.getInt("ID"));
                p.setNome(lista.getString("Nome"));
                p.setQuant(lista.getInt("Quant"));
                p.setValor(lista.getDouble("Valor"));
            }
            lista.close();
            sql.close();
            
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"ERRO no mysql !!!");
            
        }
        return p; 
    }
    
    public  List<Produto> Listar(){
        List<Produto> produtos = new ArrayList<>();
        
        try{
            sql = conexao.prepareStatement("select * from produtos");
            lista = sql.executeQuery();
            
            while(lista.next()){
               
                Produto p = new Produto();
                p.setId(lista.getInt("id"));
                p.setNome(lista.getString("Nome"));
                p.setQuant(lista.getInt("Quant"));
                p.setValor(lista.getDouble("Valor"));

                produtos.add(p);
 
            }
            lista.close();
            sql.close();
                
        }
        catch(Exception e){
                JOptionPane.showMessageDialog(null,"Erro no mysql");
        }
        
        return produtos;
    }
    
    
    public boolean Delete(int id){
    try{
        sql = conexao.prepareStatement("DELETE FROM produtos WHERE ID = ?");
        sql.setInt(1, id);

        int linhas = sql.executeUpdate();
        sql.close();

        return linhas > 0; // true = excluiu | false = id não existe

    } catch(Exception e){
        JOptionPane.showMessageDialog(null,"Erro ao deletar: " + e.getMessage());
        return false;
    }
}
    
    public boolean Alterar(Produto p){
    try{
        sql = conexao.prepareStatement("UPDATE produtos SET Nome = ?, Quant = ?, Valor = ?");

        sql.setString(1, p.getNome());
        sql.setInt(2, p.getQuant());
        sql.setDouble(3, p.getValor());
       

        int linhas = sql.executeUpdate();
        sql.close();

        return linhas > 0;

    } catch (Exception e){
        JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + e.getMessage());
        return false;
    }
}
    
    public double Somar() {
    double total = 0;

    try {
        sql = conexao.prepareStatement("SELECT SUM(valor * quant) AS total_estoque FROM produtos");
        lista = sql.executeQuery();

        if (lista.next()) {
            total = lista.getDouble("total_estoque");
        }

        lista.close();
        sql.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Erro ao somar valores!");
    }

    return total;
}




    
}


