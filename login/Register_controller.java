/*
try{
            Sql_connection db = new Sql_connection();
            
            //System.out.println(db.getClass().getName());
            String[] params = {email.getText(), password.getText()};
            ResultSet result = db.select_query("SELECT  * from users", params);
            while (result.next()) {
                System.out.println(result.getString("username"));
            }
            db.closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
*/