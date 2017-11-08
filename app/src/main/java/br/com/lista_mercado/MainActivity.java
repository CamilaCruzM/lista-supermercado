package br.com.lista_mercado;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText txtProduto;
    ProdutoAdapter adapter;

    View.OnClickListener click_ck = new View.OnClickListener(){
        @Override
        public void onClick(View view){

            CheckBox CK = (CheckBox) view;

            int posicao = (int) CK.getTag();

            Produto produtoSelecionado = adapter.getItem(posicao);

            Produto produtoDB = Produto.findById(Produto.class,produtoSelecionado.getId());

            if (CK.isChecked()){

                produtoDB.setAtivo(true);
                produtoDB.save();

                produtoSelecionado.setAtivo(true);


            }else {

                produtoDB.setAtivo(false);
                produtoDB.save();
                produtoSelecionado.setAtivo(false);

               
            }

            adapter.notifyDataSetChanged();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list_view);
        txtProduto = (EditText)findViewById(R.id.txtProduto);

        List<Produto> lstProdutos = Produto.listAll(Produto.class);

        //DADOS FAKE
        /*lstProdutos.add(new Produto("Arroz", false));
        lstProdutos.add(new Produto("Feijão", false));
        lstProdutos.add(new Produto("Macarrão", true));*/
        adapter = new ProdutoAdapter(this, lstProdutos);

        listView.setAdapter(adapter);

    }

    public void inserirItem(View view) {
        String produto = txtProduto.getText().toString();

        Produto p = new Produto (produto, false);

        if(produto.isEmpty()) return;

        //ADD NA LISTA
        adapter.add(p);

        //ADD NO BANCO
        p.save();

        //LIMPAR A CAIXINHA
        txtProduto.setText(null);
    }


    //CLASSE DO ADAPTER
    private class ProdutoAdapter extends ArrayAdapter<Produto>{
        public ProdutoAdapter(Context ctx, List<Produto> itens){
            super(ctx,0,itens);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {

            View v = convertView;

            if(v == null){
                v = LayoutInflater.from(getContext()).inflate(R.layout.item_lista, null);
            }

            Produto item = getItem(position);

            TextView txtItemOculto = v.findViewById(R.id.txtItemProduto);
            CheckBox chk_item_produto = v.findViewById(R.id.ckItemProduto);

            txtItemOculto.setText(item.getNome());
            chk_item_produto.setChecked(item.getAtivo());

            //guarda a posicao do item
            chk_item_produto.setTag(position);

            chk_item_produto.setOnClickListener(click_ck);

            return v;
        }
    }


}
