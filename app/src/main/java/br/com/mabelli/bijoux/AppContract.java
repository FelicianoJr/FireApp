package br.com.mabelli.bijoux;

/**
 * Created by feliciano on 07/03/17.
 */

public interface AppContract {

    interface View {
        void addPath(Product product);

        void clearView();

        void setAction(ActionListener listener);
    }

    interface ActionListener {

        void getnecklace();
        void getErraing();

    }
}
