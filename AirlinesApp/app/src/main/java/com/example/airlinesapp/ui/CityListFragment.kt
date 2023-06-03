package com.example.airlinesapp.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.airlinesapp.R
import com.example.airlinesapp.data.Airline
import com.example.airlinesapp.data.City
import com.example.airlinesapp.databinding.FragmentCityListBinding
import com.example.airlinesapp.models.CityListViewModel
import com.example.airlinesapp.repository.AppRepository
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

const val CITY_LIST_TAG = "CityListFragment"

// Фрагмент списка городов, привязанный к определённой авиакомпании
class CityListFragment private constructor(): Fragment() {

    // Приватное свойство для хранения ссылки на объект привязки
    private var _binding: FragmentCityListBinding? = null

   // ---

    private var miDelAirlane: MenuItem? = null
    private var miModAirlane: MenuItem? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu_city, menu)
        miDelAirlane = menu?.findItem(R.id.cityDelBtn)
        miModAirlane = menu?.findItem(R.id.cityEditBtn)
        //miAddAirline = menu?.findItem(R.id.cityAddBtn)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var per = -1
        when (item?.itemId) {
            R.id.cityDelBtn -> {
                per = 0
            }
            R.id.cityEditBtn -> {
                per = 1
            }
            //R.id.cityAddBtn -> {

            //}
        }
        if(binding.tabCity.tabCount > 0){
            if (per == 0)
                commitDeleteDialog(_city!!)
            if (per == 1)
            { editCreateDialog(_city)
            }
        }
        else
            Toast.makeText(requireContext(), "Список городов пуст", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }
    //---

    // Свойство для получения привязки (избегание работы с null)
    private val binding
        get() = _binding!!

    companion object {
        // Идентификатор авиакомпании
        private lateinit var id: UUID
        // Текущий выбранный город
        private var _city: City? = null

        // Функция для создания нового экземпляра фрагмента с передачей id авиакомпании
        fun newInstance(id: UUID): CityListFragment {
            this.id = id
            return CityListFragment()
        }

        // Свойство для получения id авиакомпании
        val getAirlineId
            get() = id
    }

    // ViewModel для работы с данными
    private lateinit var viewModel: CityListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
        }
    }

    // Инициализация представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Инициализация действий, которые произойдут после создания представления
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[CityListViewModel::class.java]

        // Установка id авиакомпании в ViewModel
        viewModel.setAirlineId(getAirlineId)

        // Отслеживание изменений авиакомпании
        viewModel.airline.observe(viewLifecycleOwner) {
            // Обновление UI при изменении данных авиакомпании
            updateUI(it)
            // Установка заголовка
            callbacks?.setTitle(it?.name ?: "")
        }

        // Обработчики кликов по кнопкам добавления, редактирования и удаления города
        binding.cityAddBtn.setOnClickListener {
            // Показывает диалоговое окно для создания нового города
            editCreateDialog(null)
        }
        binding.cityEditBtn.setOnClickListener {
            // Если есть города, показывает диалоговое окно для редактирования выбранного города
            // Если нет - показывает сообщение об ошибке
            if(binding.tabCity.tabCount > 0)
                editCreateDialog(_city)
            else
                Toast.makeText(requireContext(), "Список городов пуст.", Toast.LENGTH_SHORT).show()
        }
        binding.cityDelBtn.setOnClickListener {
            // Если есть города, показывает диалоговое окно для удаления выбранного города
            // Если нет - показывает сообщение об ошибке
            if(binding.tabCity.tabCount > 0)
                commitDeleteDialog(_city!!)
            else
                Toast.makeText(requireContext(), "Список городов пуст.", Toast.LENGTH_SHORT).show()
        }
    }

    // Позиция текущего выбранного таба
    private var tabPosition: Int = 0

    // Функция для обновления UI при изменении данных авиакомпании
    private fun updateUI(airline: Airline?) {
        // Очистка текущих слушателей и табов
        binding.tabCity.clearOnTabSelectedListeners()
        binding.tabCity.removeAllTabs()

        // Добавление табов для каждого города в авиакомпании
        for (i in 0 until (airline?.cities?.size ?: 0)) {
            binding.tabCity.addTab(binding.tabCity.newTab().apply {
                text = i.toString()
            })
        }

        // Установка адаптера и медиатора для работы с табами и ViewPager
        val adapter = GroupPageAdapter(requireActivity(), airline!!)
        binding.vpCity.adapter = adapter
        TabLayoutMediator(binding.tabCity, binding.vpCity, true, true) { tab, pos ->
            tab.text = airline.cities.get(pos).name
        }.attach()

        // Выбор текущего таба
        if (tabPosition < binding.tabCity.tabCount)
            binding.tabCity.selectTab(binding.tabCity.getTabAt(tabPosition))
        else
            binding.tabCity.selectTab(binding.tabCity.getTabAt(tabPosition - 1))

        // Установка текущего города
        if ((airline.cities.size) > 0){
            _city = airline.cities[tabPosition]
        }

        // Установка слушателя на выбор таба
        binding.tabCity.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // При выборе таба - обновление текущего города
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
                _city = airline.cities[tabPosition]
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    // Адаптер для отображения списка городов
    private inner class GroupPageAdapter(fa: FragmentActivity, private val airline: Airline) :
        FragmentStateAdapter(fa) {

        // Получение количества городов
        override fun getItemCount(): Int {
            return airline.cities.size
        }

        // Создание фрагмента для каждого города
        override fun createFragment(position: Int): Fragment {
            return FlightListFragment.newInstance(airline.id, airline.cities[position].id)
        }
    }

    // Функция для показа диалогового окна редактирования/создания города
    private fun editCreateDialog(city: City?){
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.city_dialog, null)
        builder.setView(dialogView)
        val etCityName = dialogView.findViewById(R.id.etCityName) as EditText
        if(city != null){
            builder.setTitle("          Редактирование города")
            etCityName.setText(city.name)
        }
        else
            builder.setTitle("          Добавление города")
        builder.setPositiveButton(getString(R.string.commit)) { _, _, ->
            var p = true
            etCityName.text.toString().trim().ifBlank {
                p = false
                etCityName.error = "Укажите значение"
            }
            if (p) {
                if(city != null) {
                    for (flight in city.flights)
                        flight.departureCity = etCityName.text.toString().trim()
                    viewModel.editCity(getAirlineId, city.id, etCityName.text.toString().trim())
                    Toast.makeText(requireContext(), "Город обновлён.", Toast.LENGTH_SHORT).show()
                }
                else {
                    viewModel.newCity(getAirlineId, etCityName.text.toString().trim())
                    Toast.makeText(requireContext(), "Город добавлен.", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(requireContext(), "Заполните все поля.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(R.string.cancel, null)
        val alert = builder.create()
        alert.show()
    }

    // Функция для показа диалогового окна удаления города
    private fun commitDeleteDialog(city: City) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить город ${city.name} из списка ?")
        builder.setTitle("                  Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            viewModel.deleteCity(getAirlineId, city)
            Toast.makeText(requireContext(), "Город удалён.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.cancel, null)
        builder.show()
    }

    // Интерфейс для взаимодействия с активностью
    interface Callbacks {
        fun setTitle(_title: String)
    }

    // Ссылка на объект, реализующий интерфейс Callbacks
    var callbacks: Callbacks? = null

    // При присоединении фрагмента - инициализация ссылки на callbacks
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    // При отсоединении фрагмента - обнуление ссылки на callbacks
    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }
}