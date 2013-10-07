/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Movable.objects;

/**
 *
 * @author oivhe_000
 */
abstract class  MObject {
    
    int posX, posY, posZ, rad;
    
    
    public void SetPos(int x, int y, int z){
        posX = x;
        posY = y;
        posZ = z;
        
    }
    
    public void Rotate(int r){
        rad=r;
    }
    
    
}
