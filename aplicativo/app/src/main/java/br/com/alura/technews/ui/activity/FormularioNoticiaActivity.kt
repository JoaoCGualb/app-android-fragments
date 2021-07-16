package br.com.alura.technews.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.mostraErro
import br.com.alura.technews.ui.viewmodel.FormularioNoticiaViewModel
import kotlinx.android.synthetic.main.activity_formulario_noticia.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TITULO_APPBAR_EDICAO = "Editando notícia"
private const val TITULO_APPBAR_CRIACAO = "Criando notícia"
private const val MENSAGEM_ERRO_SALVAR = "Não foi possível salvar notícia"

class FormularioNoticiaActivity : AppCompatActivity() {

    private val noticiaId: Long by lazy {
        intent.getLongExtra(NOTICIA_ID_CHAVE, 0)
    }

    private val viewModel: FormularioNoticiaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_noticia)
        Log.i("teste", "Dentro do formulario")
        definindoTitulo()
        preencheFormulario()
    }

    private fun definindoTitulo() {
        title = if (noticiaId > 0) {
            TITULO_APPBAR_EDICAO
        } else {
            TITULO_APPBAR_CRIACAO
        }
    }

    private fun preencheFormulario() {
        viewModel.buscarId(noticiaId).observe(this, { noticiaEncontrada->
            if (noticiaEncontrada != null) {
                formulario_noticia_titulo.setText(noticiaEncontrada.titulo)
                formulario_noticia_texto.setText(noticiaEncontrada.texto)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.formulario_noticia_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.formulario_noticia_salva -> {
                val titulo = formulario_noticia_titulo.text.toString()
                val texto = formulario_noticia_texto.text.toString()
                salva(Noticia(noticiaId, titulo, texto))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun salva(noticia: Noticia) {
        viewModel.salva(noticia).observe(this, {
            if (it.erro == null){
                finish()
            }else{
                mostraErro(MENSAGEM_ERRO_SALVAR)
            }
        })
    }


}
