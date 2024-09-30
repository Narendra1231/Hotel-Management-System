import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Hotel_Reservation {
    public static String url="jdbc:mysql://localhost:3306/hotel_db";
    public static String username="root";
    public static String password="root";
    public static void main(String args[]){
          try{
               Connection conect=DriverManager.getConnection(url, username, password);
               System.out.println("Connection established !");
               while(true){
                System.out.println();
                System.out.println("Hotel Management System ");
                Scanner scanner=new Scanner(System.in);
                System.out.println("1.Reserve a room.");
                System.out.println("2.View reservations.");
                System.out.println("3.Get room number."); 
                System.out.println("4.Update reservations");
                System.out.println("5.Delete Reservations");
                System.out.println("0.Exit");
                System.out.println("Choose an option.");
                int choice=scanner.nextInt();
                switch(choice){
                    case 1:
                         reserveRoom(conect, scanner);
                         break;
                    case 2:
                         viewReservations(conect);
                         break;
                    case 3:
                         getRoomNo(conect, scanner);
                         break;
                    case 4:
                         updateReservation(conect, scanner);
                         break; 
                    case 5:
                         deleteReservation(conect, scanner);   
                         break;  
                    case 0:
                         exit();     
                         scanner.close();
                         return;
                    default:
                        System.out.println("Invalid choice,please try again !");
                }
                  

               }
          }
          catch(SQLException e){
            System.out.println(e.getMessage());
          }
    }
    private static void reserveRoom(Connection connect,Scanner scanner){
              System.out.println("Enter guest name:- ");
              String guest_name=scanner.next();
              //scanner.nextLine();
              System.out.println("Enter room no.:- ");
              int room_no=scanner.nextInt();
              System.out.println("Enter contact no.:- ");
              String mob_no=scanner.next();
              String query="INSERT INTO hotel_db.roombooking(guest_name,room_no,contact_no)"+
                           "VALUES('"+guest_name+"',"+room_no+",'"+mob_no+"')";
              try{
                    Statement stmt=connect.createStatement();
                    int rowAffected=stmt.executeUpdate(query);
                    if(rowAffected>0){
                        System.out.println("Reservation is successful");
                    }
                    else{
                        System.out.println("Reservation is failed");
                    }
              }
              catch(SQLException e){
                System.out.println(e.getMessage());
              }
    }
    private static void viewReservations(Connection conect){
      String query="select * from hotel_db.roombooking";
      try{
        Statement stmt=conect.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        System.out.println("+----------------+-----------------+--------------+----------------+--------------------+");
        System.out.println("| Reservation_id | guest_name      | room_no      | contact_no     | date               |");
        System.out.println("+----------------+-----------------+--------------+----------------+--------------------+");
        while(rs.next()){
          int id=rs.getInt("id");
          String name=rs.getString("guest_name");
          int room_no=rs.getInt("room_no");
          String mob_no=rs.getString("contact_no");
          String date=rs.getString("reservation_date");
          System.out.printf("| %-15d| %-16s| %-13d| %-15s| %-15s| \n",id,name,room_no,mob_no,date);
          System.out.println("+----------------+-----------------+--------------+----------------+--------------------+");
        }
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
      }
    }
    private static void getRoomNo(Connection conect,Scanner scanner){
      System.out.println("Enter reservation_id:- ");
      int r_id=scanner.nextInt();
      System.out.println("Enter guest_name:-");
      String guest_name=scanner.next(); 
      String sql= "SELECT room_no FROM hotel_db.roombooking WHERE id = "+ r_id
      +" AND guest_name = '" + guest_name +"'"; 
    
     try{
          Statement stmt=conect.createStatement();
          ResultSet rs=stmt.executeQuery(sql);
          if(rs.next()){
            int room_no=rs.getInt("room_no");
            System.out.println("Room_no for given reservation id:-"+r_id+" and guest_name:- "+guest_name+" ="+room_no);
          }
          else{
            System.out.println("reservation not found for given id and name.");
          }
      }
      catch(SQLException e){
             System.out.println(e.getMessage());
      }

    }
  
    private static void updateReservation(Connection conect,Scanner scanner){
          System.out.println("Enter reservation_id to update");
          int reservation_id=scanner.nextInt();
          scanner.nextLine();
          if(!reservationExists(conect, reservation_id)){
            System.out.println("reservation does not exixts !");
            return;
          }
          System.out.println("Enter new guest name:-");
          String new_guest_name=scanner.next();
          System.out.println("Enter new room no:- ");
          int new_room_no=scanner.nextInt();
          System.out.println("Enter new Mobile No.:- ");
          String new_mob_no=scanner.next();
          String sql="update hotel_db.roombooking set guest_name='"+new_guest_name+"',"+
           "room_no="+ new_room_no +","+
           "contact_no='"+new_mob_no+"'"+
           "where id="+reservation_id;         
          try{
            Statement stmt=conect.createStatement();
            int rowAffected=stmt.executeUpdate(sql);
            if(rowAffected>0){
              System.out.println("reservation updated successfully !");
            }
            else{
              System.out.println("updation is failed !");
            }
          }
          catch(SQLException e){
            System.out.println(e.getMessage());
          }
    }
    private static void deleteReservation(Connection conect,Scanner scanner){
      System.out.println("Enter reservation id to delete:-");
      int reservation_id=scanner.nextInt();
      String sql="delete from hotel_db.roombooking where id="+reservation_id;
      if(!reservationExists(conect, reservation_id)){
        System.out.println("reservation does not exist !");
        return;
      }
      try{
        Statement stmt=conect.createStatement();
        int rowAffected=stmt.executeUpdate(sql);
        if(rowAffected>0){
          System.out.println("Deletion is successful");
        }
        else{
          System.out.println(("deletion is failed !"));
        }
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
      }
    }
    private static boolean reservationExists(Connection conect,int reservation_id){
      String sql="select id from hotel_db.roombooking where id="+reservation_id;
      try{
               Statement stmt=conect.createStatement();
               ResultSet rs=stmt.executeQuery(sql);
               return rs.next();
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
        e.printStackTrace();
        return false; 
      }
    }
    private static void exit(){
      try{
        System.out.print("Exiting system.");
        int i=5;
        while(i!=0){
          System.out.print(".");
          Thread.sleep(400);
          i--;
        }
        System.out.println();
      System.out.println("thank you for using Hotel Management System !");
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
      
    }
}
