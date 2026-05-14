/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ASUS
 */
public class AdminModel extends UserModel {
    public AdminModel() {

    }

    public AdminModel(
            String username,
            String password
    ) {

        super(
                username,
                password
        );

    }

    @Override
    public void login() {

        System.out.println(
                "Admin Login"
        );

    }

}
