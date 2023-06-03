package com.example.airlinesapp.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.R
import com.example.airlinesapp.data.Flight
import com.example.airlinesapp.data.Plane
import com.example.airlinesapp.data.Seat
import com.example.airlinesapp.databinding.FragmentFlightListBinding
import com.example.airlinesapp.models.FlightListViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class FlightListFragment private constructor(): Fragment() {

    // Binding объект для доступа к представлениям фрагмента
    private var _binding: FragmentFlightListBinding? = null
    private val binding
        get() = _binding!!

    // Адаптер для RecyclerView
    private var adapter: FlightListFragment.FlightListAdapter = FlightListAdapter(emptyList())

    // Компаньон для создания нового экземпляра фрагмента и получения ID авиакомпании и города
    companion object {
        private lateinit var airlineID: UUID
        private lateinit var cityID: UUID
        private var _flight: Flight? = null
        fun newInstance(airlineID: UUID, cityID: UUID): FlightListFragment {
            this.airlineID = airlineID
            this.cityID = cityID
            return FlightListFragment()
        }

        // Геттеры для получения ID авиакомпании и города
        val getAirlineID
            get() = airlineID
        val getCityId
            get() = cityID
    }

    // Ссылка на ViewModel
    private lateinit var viewModel: FlightListViewModel

    // Жизненный цикл фрагмента: onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    // Жизненный цикл фрагмента: onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFlightListBinding.inflate(inflater, container, false)
        binding.rvFlight.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    // Жизненный цикл фрагмента: onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FlightListViewModel::class.java]
        viewModel.setAirlineAndCityID(getAirlineID, getCityId)
        viewModel.city.observe(viewLifecycleOwner) {
            adapter = FlightListAdapter(it?.flights ?: emptyList())
            binding.rvFlight.adapter = adapter
            //callbacks?.setTitle(it?.name ?: "")
        }
        binding.flightAddBtn.setOnClickListener {
            editCreateDialog(null)
        }
    }
    // ViewHolder для отображения элемента списка рейсов (LC)
    /*
    private inner class FlightHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var flight: Flight

        // Метод привязки данных к элементу списка
        fun bind(flight: Flight) {
            this.flight = flight
            itemView.findViewById<TextView>(R.id.tvElement).text = flight.departureCity.plus(" -> ")
                .plus(flight.arrivalCity)
            itemView.findViewById<ConstraintLayout>(R.id.crudButtons).visibility = View.GONE
        }

        // Обработка кликов на элементе списка
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener {
                // Обработка долгого нажатия
                val cl = itemView.findViewById<ConstraintLayout>(R.id.crudButtons)

                cl.visibility = View.VISIBLE
                lastItemView?.findViewById<ConstraintLayout>(R.id.crudButtons)?.visibility =
                    View.GONE
                lastItemView = if (lastItemView == itemView) null else itemView
                if (cl.visibility == View.VISIBLE) {
                    itemView.findViewById<ImageButton>(R.id.delBtn).setOnClickListener {
                        commitDeleteDialog(flight)
                    }
                    itemView.findViewById<ImageButton>(R.id.editBtn).setOnClickListener {
                        editCreateDialog(flight)
                    }
                }
                true // Возвращаем true, чтобы сигнализировать, что событие было обработано
            }
        }

        // Обработка клика на элементе списка
        override fun onClick(v: View?) {
            buyTicketDialog(flight)
        }
    }*/

    // ViewHolder для отображения элемента списка рейсов
    private inner class FlightHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var flight: Flight

        // Метод привязки данных к элементу списка
        fun bind(flight: Flight) {
            this.flight = flight
            itemView.findViewById<TextView>(R.id.tvElement).text = flight.departureCity.plus(" -> ")
                .plus(flight.arrivalCity)
            itemView.findViewById<ConstraintLayout>(R.id.crudButtons).visibility = View.GONE
        }

        // Обработка кликов на элементе списка
        init {
            itemView.findViewById<TextView>(R.id.tvElement).setOnClickListener {
                buyTicketDialog(flight)
            }

            itemView.setOnClickListener {
                // Обработка клика
                val cl = itemView.findViewById<ConstraintLayout>(R.id.crudButtons)

                cl.visibility = if (cl.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                lastItemView?.findViewById<ConstraintLayout>(R.id.crudButtons)?.visibility = View.GONE
                lastItemView = if (lastItemView == itemView) null else itemView
                if (cl.visibility == View.VISIBLE) {
                    itemView.findViewById<ImageButton>(R.id.delBtn).setOnClickListener {
                        commitDeleteDialog(flight)
                    }
                    itemView.findViewById<ImageButton>(R.id.editBtn).setOnClickListener {
                        editCreateDialog(flight)
                    }
                }
            }
        }

        override fun onClick(v: View) {}
    }





    // Последний выбранный элемент списка
    private var lastItemView: View? = null

    // Функция для отображения диалога покупки билета
    private fun buyTicketDialog(flight: Flight) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.buy_ticket_dialog, null)
        builder.setView(dialogView)
        val spinDate = dialogView.findViewById(R.id.spinDate) as Spinner
        val spinSeat = dialogView.findViewById(R.id.spinSeat) as Spinner
        val tvDayOfWeek = dialogView.findViewById(R.id.tvDayOfWeek) as TextView
        val tvTypeOfPlane = dialogView.findViewById(R.id.tvTypeOfPlane) as TextView
        val tvTime = dialogView.findViewById(R.id.tvTime) as TextView
        val tvTimeFlight = dialogView.findViewById(R.id.tvTimeFlight) as TextView

        tvDayOfWeek.setText("День недели: ${flight.dayOfWeek}")
        tvTypeOfPlane.setText("Вы полетите на ${flight.nameOfPlane}")
        tvTime.setText("Отправление в ${flight.hour}:${flight.minute}")

        val departureTime = LocalTime.of(flight.hour, flight.minute)
        val flightDurationMinutes = flight.flightTime
        val arrivalTime = departureTime.plusMinutes(flightDurationMinutes.toLong())

        tvTimeFlight.setText("Прибытие в ${arrivalTime.hour}:${arrivalTime.minute}")

        val dates = ArrayList<String>()
        for (plane in flight.planes) {
            dates.add(plane.date)
        }

        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dates)
        spinDate.adapter = adapter1

        spinDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val date = spinDate.getItemAtPosition(position) as String
                val plane = flight.planes.find { it.date == date }
                val seats = ArrayList<Seat>()
                seats.add(0, Seat(name = "Не выбрано"))
                for (seat in plane?.seats!!)
                    seats.add(seat)
                spinSeat.adapter = SeatAdapter(requireContext(), seats)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
        builder.setTitle(flight.departureCity.plus(" -> ").plus(flight.arrivalCity))
        builder.setPositiveButton("Приобрести") { _, _, ->
            if (spinSeat.selectedItem.toString() == "Не выбрано") {
                Toast.makeText(requireContext(), "Укажите все значения.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.pay_ticket(
                    flight.id,
                    spinDate.selectedItem.toString(),
                    spinSeat.selectedItem.toString()
                )
                Toast.makeText(requireContext(), "Вы приобрели билет.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(R.string.cancel, null)
        val alert = builder.create()
        alert.show()
    }


    // Этот адаптер управляет отображением элементов в выпадающем списке мест.
    class SeatAdapter(context: Context, seats: List<Seat>) : ArrayAdapter<Seat>(context, android.R.layout.simple_spinner_item, seats) {
        // Здесь мы устанавливаем представление для каждого элемента выпадающего списка.
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent)

            // Здесь мы получаем элемент Seat для данной позиции.
            val seat = getItem(position)
            if (seat != null) {
                // Мы устанавливаем цвет фона в зависимости от того, свободно ли место.
                val color = if (seat.isFree) Color.GREEN else Color.RED
                view.setBackgroundColor(color)

                // Мы делаем место доступным для выбора только если оно свободно.
                view.isEnabled = seat.isFree

                // Мы делаем место некликабельным, если оно занято.
                view.isClickable = !seat.isFree
            }

            return view
        }
    }

    // Эта функция показывает диалоговое окно для редактирования или создания нового рейса.
    private fun editCreateDialog(flight: Flight?){
        // Создаем экземпляр AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.flight_dialog, null)
        builder.setView(dialogView)
        val tvDepartureCity = dialogView.findViewById(R.id.tvDepartureCity) as TextView
        val etArrivalCity = dialogView.findViewById(R.id.etArrivalCity) as EditText
        val etFlightTime = dialogView.findViewById(R.id.etFlightTime) as EditText
        val spinDayOfWeek = dialogView.findViewById(R.id.spinDayOfWeek) as Spinner
        val spinTypeOfPlane = dialogView.findViewById(R.id.spinTypeOfPlane) as Spinner
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)

        tvDepartureCity.text = viewModel.getNameOfCity()

        val daysOfWeek = listOf("Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота", "Воскресенье")

        val availableAirplaneTypes = listOf(
            "Douglas DC-3",
            "Boeing 737",
            "Airbus A320"
        )

        val typeOfPlaneMap = mapOf(
            "Douglas DC-3" to 50,
            "Boeing 737" to 100,
            "Airbus A320" to 150
        )

        val dayOfWeekMap = mapOf(
            "Понедельник" to DayOfWeek.MONDAY,
            "Вторник" to DayOfWeek.TUESDAY,
            "Среда" to DayOfWeek.WEDNESDAY,
            "Четверг" to DayOfWeek.THURSDAY,
            "Пятница" to DayOfWeek.FRIDAY,
            "Суббота" to DayOfWeek.SATURDAY,
            "Воскресенье" to DayOfWeek.SUNDAY
        )

        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, daysOfWeek)
        spinDayOfWeek.adapter = adapter1
        val adapter2 = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, availableAirplaneTypes)
        spinTypeOfPlane.adapter = adapter2

        if(flight != null){
            builder.setTitle("              Редактирование рейса")
            etArrivalCity.setText(flight.arrivalCity)
            etFlightTime.setText(flight.flightTime.toString())
            spinDayOfWeek.setSelection(adapter1.getPosition(flight.dayOfWeek))
            spinTypeOfPlane.setSelection(adapter2.getPosition(flight.nameOfPlane))
            timePicker.hour = flight.hour
            timePicker.minute = flight.minute
        }
        else
            builder.setTitle("              Добавление рейса")
        builder.setPositiveButton(getString(R.string.commit)) { _, _, ->
            var p = true
            etArrivalCity.text.toString().trim().ifBlank {
                p = false
                etArrivalCity.error = "Укажите название города"
            }
            etFlightTime.text.toString().trim().ifBlank {
                p = false
                etFlightTime.error = "Укажите время полета"
            }
            if (p) {
                val startDate = LocalDate.now()
                val endDate = startDate.plusDays(150)
                val datesInRange = mutableListOf<LocalDate>()
                var date = startDate
                while (!date.isAfter(endDate)) {
                    datesInRange.add(date)
                    date = date.plusDays(1)
                }

                val dayOfWeek = dayOfWeekMap[spinDayOfWeek.selectedItem.toString()]
                val days = datesInRange.filter { it.dayOfWeek == dayOfWeek }
                val planes = ArrayList<Plane>()
                for (day in days){
                    val numberOfSeats = typeOfPlaneMap[spinTypeOfPlane.selectedItem.toString()] ?: 0
                    val numberOfRows = (typeOfPlaneMap[spinTypeOfPlane.selectedItem.toString()] ?: 0) / 6
                    val seats = ArrayList<Seat>()
                    for (i in 1 until numberOfRows + 1) {
                        seats.add(Seat(name = i.toString().plus("A")))
                        seats.add(Seat(name = i.toString().plus("B")))
                        seats.add(Seat(name = i.toString().plus("C")))
                        seats.add(Seat(name = i.toString().plus("D")))
                        seats.add(Seat(name = i.toString().plus("E")))
                        seats.add(Seat(name = i.toString().plus("F")))
                    }
                    val plane = Plane(date = day.toString(),
                        numberOfSeats = numberOfSeats,
                        numberOfRows = numberOfRows,
                        seats = seats
                    )
                    planes.add(plane)
                }
                val newFlight = Flight(
                    departureCity = tvDepartureCity.text.toString().trim(),
                    arrivalCity = etArrivalCity.text.toString().trim(),
                    nameOfPlane = spinTypeOfPlane.selectedItem.toString().trim(),
                    dayOfWeek = spinDayOfWeek.selectedItem.toString().trim(),
                    flightTime = etFlightTime.text.toString().toInt(),
                    planes = planes,
                    hour = timePicker.hour,
                    minute = timePicker.minute
                )
                if(flight != null) {
                    if (newFlight.nameOfPlane == flight.nameOfPlane)
                        newFlight.planes =flight.planes
                    viewModel.editFlight(flight.id, newFlight)
                    Toast.makeText(requireContext(), "Рейс обновлён.", Toast.LENGTH_SHORT).show()
                }
                else {
                    viewModel.newFlight(newFlight)
                    Toast.makeText(requireContext(), "Рейс добавлен.", Toast.LENGTH_SHORT).show()
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

    // Эта функция показывает диалоговое окно для подтверждения удаления рейса.
    private fun commitDeleteDialog(flight: Flight) {
        // Создаем экземпляр AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить рейс ${flight.departureCity.plus(" -> ")
            .plus(flight.arrivalCity)} из списка ?")
        builder.setTitle("                  Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            viewModel.deleteFlight(flight)
            Toast.makeText(requireContext(), "Рейс удалён.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.cancel, null)
        builder.show()
    }

    // Этот адаптер управляет отображением элементов в RecyclerView.
    private inner class FlightListAdapter(private val items: List<Flight>) :
        RecyclerView.Adapter<FlightHolder>() {
        // Создаем новый ViewHolder для представления элемента.
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FlightHolder {
            val view = layoutInflater.inflate(R.layout.element_flight_list, parent, false)
            return FlightHolder(view)
        }

        // Возвращаем количество элементов в списке.
        override fun getItemCount(): Int = items.size

        // Привязываем данные к ViewHolder.
        override fun onBindViewHolder(holder: FlightHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    // Этот интерфейс позволяет фрагменту взаимодействовать с его родительской активностью.
    interface Callbacks {
        fun setTitle(_title: String)
        //fun showCity(airlineID: UUID, cityID: UUID)
    }

    var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }
}


//---

/*
interface Callbacks {
        fun setTitle(_title: String)
        //fun showCity(airlineID: UUID, cityID: UUID)
    }

    //        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            val view = super.getView(position, convertView, parent)
//
//            val seat = getItem(position)
//            if (seat != null) {
//                val color = if (seat.isFree) Color.GREEN else Color.RED
//                view.setBackgroundColor(color)
//                view.isEnabled = seat.isFree // Здесь мы отключаем выбор занятых мест
//                if (! seat.isFree)
//                    view.isClickable = false
//            }
//
//            return view
//        }
    }
 */