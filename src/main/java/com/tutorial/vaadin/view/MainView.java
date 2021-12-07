package com.tutorial.vaadin.view;

import com.tutorial.vaadin.component.EmployeeEditor;
import com.tutorial.vaadin.domain.Employee;
import com.tutorial.vaadin.repository.EmployeeRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "")
public class MainView extends VerticalLayout {
    private final EmployeeRepository employeeRepository;
    private Grid<Employee> grid = new Grid<>(Employee.class);

    private final TextField filter = new TextField();
    private final Button addNewButton = new Button("New employee", VaadinIcon.PLUS.create());
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewButton);

    private final EmployeeEditor editor;

    @Autowired
    public MainView(EmployeeRepository employeeRepository, EmployeeEditor editor) {
        this.employeeRepository = employeeRepository;
        this.editor = editor;

        filter.setPlaceholder("Type to filter");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> showEmployee(field.getValue()));
        grid.removeColumn(grid.getColumnByKey("id"));
        add(toolbar, grid, editor);

        // Подключение выбранного клиента к редактору или скрыть если ни один не выбран
        grid
                .asSingleSelect()
                .addValueChangeListener(e -> { editor.editEmployee(e.getValue()); });

        // Создание и редактирование нового клиента, нажав на кнопку Add new
        addNewButton
                .addClickListener(e -> editor.editEmployee(new Employee()));

        // Листнер обновления данных для  того чтобы после изменения сделанные редактором
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showEmployee(filter.getValue());
        });


        showEmployee("");

    }

    private void showEmployee(String name) {
        if (name.isEmpty()) {
            grid.setItems(employeeRepository.findAll());
        } else {
            grid.setItems(employeeRepository.findByName(name));
        }
    }

}
