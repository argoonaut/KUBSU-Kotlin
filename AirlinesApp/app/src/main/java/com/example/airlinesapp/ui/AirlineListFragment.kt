package com.example.airlinesapp.ui

import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.FragmentAirlineListBinding
import com.example.airlinesapp.models.AirlineListViewModel
import com.example.airlinesapp.R
import com.example.airlinesapp.data.Airline
import java.util.*

const val AIRLINE_LIST_TAG = "AirlineListFragment"
const val AIRLINE_LIST_TITLE = "Авиакомпании"

class AirlineListFragment : Fragment() {
    // ViewModel фрагмента
    private lateinit var viewModel: AirlineListViewModel
    // Binding фрагмента
    private var _binding: FragmentAirlineListBinding? = null
    // Non-null binding для использования в коде
    private val binding
        get() = _binding!!

    // Адаптер для RecyclerView списка авиакомпаний
    private var adapter: AirlineListAdapter = AirlineListAdapter(emptyList())

    companion object {
        // Функция для создания нового экземпляра этого фрагмента
        fun newInstance() = AirlineListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализация binding'а
        _binding = FragmentAirlineListBinding.inflate(inflater, container, false)
        // Установка layout manager для RecyclerView
        binding.rvAirline.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[AirlineListViewModel::class.java]
        // Наблюдение за данными в ViewModel
        viewModel.airlines.observe(viewLifecycleOwner) {
            // Обновление адаптера при изменении данных
            adapter = AirlineListAdapter(it)
            binding.rvAirline.adapter = adapter
        }
        // Установка заголовка
        callbacks?.setTitle(AIRLINE_LIST_TITLE)

        // Обработчик клика по кнопке добавления авиакомпании
        binding.airlineAddBtn.setOnClickListener {
            // Отображение диалогового окна для добавления авиакомпании
            editCreateDialog(null)
            Log.d("AirlineListFragment", "message")
        }
    }

    // Holder для элементов списка в RecyclerView
    private inner class AirlineHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var airline: Airline

        // Привязка данных авиакомпании к элементу списка
        fun bind(airline: Airline) {
            this.airline = airline
            itemView.findViewById<TextView>(R.id.tvElement).text = airline.name
            itemView.findViewById<ConstraintLayout>(R.id.crudButtons).visibility = View.GONE
        }

        init {
            itemView.setOnClickListener(this)
        }

        // Обработка клика по элементу списка
        override fun onClick(v: View?) {
            // Управление видимостью элементов управления (CRUD)
            val cl = itemView.findViewById<ConstraintLayout>(R.id.crudButtons)
            cl.visibility = View.VISIBLE
            lastItemView?.findViewById<ConstraintLayout>(R.id.crudButtons)?.visibility = View.GONE
            lastItemView = if (lastItemView == itemView) null else itemView
            // Назначение обработчиков на кнопки управления
            if (cl.visibility == View.VISIBLE) {
                itemView.findViewById<ImageButton>(R.id.openBtn).setOnClickListener {
                    callbacks?.showAirline(airline.id)
                }
                itemView.findViewById<ImageButton>(R.id.delBtn).setOnClickListener {
                    commitDeleteDialog(airline)
                }
                itemView.findViewById<ImageButton>(R.id.editBtn).setOnClickListener {
                    editCreateDialog(airline)
                }
            }
        }
    }

    // Ссылка на последний выбранный элемент списка
    private var lastItemView: View? = null

    // Отображение диалога для создания/редактирования авиакомпании
    private fun editCreateDialog(airline: Airline?) {
        // Создание строителя диалогового окна
        val builder = AlertDialog.Builder(requireContext())
        // Позволяет пользователю отменить диалоговое окно, нажав за его пределами
        builder.setCancelable(true)
        // Инициализация вида диалогового окна
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.airline_dialog, null)
        // Установка вида для диалогового окна
        builder.setView(dialogView)

        // Инициализация полей ввода имени и года основания авиакомпании
        val etAirlineName = dialogView.findViewById(R.id.etAirlineName) as EditText
        val etAirlineYear = dialogView.findViewById(R.id.etAirlineYear) as EditText

        // Если авиакомпания не null, устанавливаем данные авиакомпании в поля ввода
        if (airline != null) {
            builder.setTitle("  Редактирование авиакомпании")
            etAirlineName.setText(airline.name)
            etAirlineYear.setText(airline.year.toString())
        }
        // Если авиакомпания null, диалоговое окно для добавления новой авиакомпании
        else
            builder.setTitle("       Добавление авиакомпании")

        // Обработка нажатия на кнопку подтверждения диалогового окна
        builder.setPositiveButton(getString(R.string.commit)) { _, _, ->
            var p = true
            // Проверяем, что поля ввода не пустые
            etAirlineName.text.toString().trim().ifBlank {
                p = false
                etAirlineName.error = "Укажите имя"
            }
            etAirlineYear.text.toString().trim().ifBlank {
                p = false
                etAirlineYear.error = "Укажите год"
            }
            // Если все поля заполнены, обновляем или добавляем авиакомпанию в зависимости от контекста
            if (p) {
                if(airline != null) {
                    // Обновляем авиакомпанию
                    viewModel.editAirline(
                        airline.id,
                        etAirlineName.text.toString().trim(),
                        etAirlineYear.text.toString().trim().toInt()
                    )
                    Toast.makeText(requireContext(), "Авиакомпания обновлена.", Toast.LENGTH_SHORT).show()
                }
                else {
                    // Добавляем новую авиакомпанию
                    viewModel.newAirline(
                        etAirlineName.text.toString().trim(),
                        etAirlineYear.text.toString().trim().toInt()
                    )
                    Toast.makeText(requireContext(), "Авиакомпания добавлена.", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                // Если какие-то поля не заполнены, выводим сообщение об ошибке
                Toast.makeText(requireContext(), "Заполните все поля.", Toast.LENGTH_SHORT).show()
            }
        }
        // Действие при нажатии на кнопку "Отмена" в диалоговом окне
        builder.setNegativeButton(R.string.cancel, null)
        // Создание и отображение диалогового окна
        val alert = builder.create()
        alert.show()
    }


    // Отображение диалога для подтверждения удаления авиакомпании
    private fun commitDeleteDialog(airline: Airline) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить авиакомпанию ${airline.name} из списка ?")
        builder.setTitle("                  Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            viewModel.deleteAirline(airline)
            Toast.makeText(requireContext(), "Авиакомпания удалена.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.cancel, null)
        builder.show()
    }

    // Адаптер для RecyclerView
    private inner class AirlineListAdapter(private val items: List<Airline>) :
        RecyclerView.Adapter<AirlineHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirlineHolder {
            val view = layoutInflater.inflate(R.layout.element_list, parent, false)
            return AirlineHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: AirlineHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    // Интерфейс для взаимодействия с активити
    interface Callbacks {
        fun setTitle(_title: String)
        fun showAirline(id: UUID)
    }

    // Ссылка на callbacks
    var callbacks: Callbacks? = null

    // Прикрепление callbacks при присоединении фрагмента
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    // Отключение callbacks при отсоединении фрагмента
    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }
}