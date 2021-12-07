package com.tutorial.vaadin.component;

import com.tutorial.vaadin.domain.Employee;
import com.tutorial.vaadin.repository.EmployeeRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class EmployeeEditor extends VerticalLayout implements KeyNotifier {
    private final EmployeeRepository employeeRepository;
    /**
     * Изменяемая сущность
     */
    private Employee employee;

    /**
     * Филды которые мы будем изменяем в сущности Employee
     */
    private TextField firstName = new TextField("", "First name");
    private TextField lastName = new TextField("", "Last name");
    private TextField patronymic = new TextField("", "Patronymic");

    /**
     * Кнопочки
     */
    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);

    /**
     * Байндер привязываем нашу текущую форму к инстансу который нам передается
     */
    private Binder<Employee> binder = new Binder<>(Employee.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public EmployeeEditor(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        add(lastName, firstName, patronymic, buttons);
        /**
         * Байндер с помощью рефликсии изменяет объект,
         * то есть переменные которые мы создали должны быть идентичны с классом Employee
         * bind using naming convention
         */
        binder.bindInstanceFields(this);
        // Добавляет интервалы между филдами
        setSpacing(true);
        // берут две кнопочки  и добавляют им темы primary и error
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        // устанавливаем листнер на кнопку Enter и отправлять все это в метод сэйв
        addKeyPressListener(Key.ENTER, e -> save());

        // устанавливаем обработчики листнеры на нажатие кнопочек
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmployee(employee));
        setVisible(false);
    }

    // Save
    private void save() {
        employeeRepository.save(employee);
        changeHandler.onChange();
    }

    // Delete
    private void delete() {
        employeeRepository.delete(employee);
        changeHandler.onChange();
    }

    // Edit
    public void editEmployee(Employee emp) {
        if (emp == null) {
            setVisible(false);
            return;
        }

        if (emp.getId() != null) {
            this.employee = employeeRepository.findById(emp.getId()).orElse(emp);
        } else {
            this.employee = emp;
        }

        binder.setBean(this.employee);

        setVisible(true);

        lastName.focus();
    }
}
