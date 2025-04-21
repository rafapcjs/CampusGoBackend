package com.CampusGo.schelude.service.implementation;

import com.CampusGo.commons.configs.error.exceptions.ConflictException;
import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.commons.configs.message.InfoMessage;
import com.CampusGo.schelude.persistencie.entity.Schelude;
import com.CampusGo.schelude.persistencie.repository.ScheludeRepository;
import com.CampusGo.schelude.presentation.dto.ListOrderScheludeDTO;
import com.CampusGo.schelude.presentation.dto.ScheludeResponseDTO;
import com.CampusGo.schelude.presentation.payload.CreateScheludeRequest;
import com.CampusGo.schelude.presentation.payload.UpdateScheludeRequest;
import com.CampusGo.schelude.service.interfaces.ScheludeService;
import com.CampusGo.subject.persistencie.entity.Subject;
import com.CampusGo.subject.persistencie.repository.SubjectRepository;  // Asegúrate de tener el repositorio de Subject importado
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheludeServiceImpl implements ScheludeService {

    private final ScheludeRepository repository;
    private final SubjectRepository subjectRepository;  // Añadido para obtener el Subject por código


    // Metodo para crear un horario

    @Override
    public ScheludeResponseDTO createSchelude(CreateScheludeRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Convertimos las horas de inicio y fin a formato 24h
        LocalTime horaIni = parseTime(request.getHoraInicial());
        LocalTime horaFin = parseTime(request.getHoraFinal());

        if (horaIni.isAfter(horaFin)) {
            throw new ConflictException("La hora inicial no puede ser posterior a la hora final.");
        }

        // Verificar si ya existe un horario que se cruce para la misma asignatura
        List<Schelude> existing = repository.findByCodeAsignatureFk(request.getCodeAsignatureFk());

        for (Schelude sch : existing) {
            if (sch.getDia().equals(request.getDia())) {
                LocalTime existingStart = parseTime(sch.getHoraInicial());
                LocalTime existingEnd = parseTime(sch.getHoraFinal());

                boolean overlaps = !horaIni.isAfter(existingEnd) && !horaFin.isBefore(existingStart);
                if (overlaps) {
                    throw new ConflictException("No es posible crear el horario, ya existe uno o se cruza.");
                }
            }
        }

        // Asignar el nuevo código secuencial
        Integer newCode = repository.findMaxCode();
        newCode = (newCode == null) ? 1 : newCode + 1;

        // Buscar el Subject por su código (code)
        Subject subject = subjectRepository.findByCode(request.getCodeAsignatureFk())
                .orElseThrow(() -> new ConflictException("La asignatura con código " + request.getCodeAsignatureFk() + " no existe."));

        // Crear y guardar el nuevo horario
        Schelude schelude = new Schelude();
        schelude.setCode(newCode);  // Asignar el código secuencial
        schelude.setSubject(subject);  // Establecer la relación con Subject a través del código
        schelude.setDia(request.getDia());
        schelude.setHoraInicial(request.getHoraInicial());
        schelude.setHoraFinal(request.getHoraFinal());

        repository.save(schelude);

        // Responder con el DTO
        return new ScheludeResponseDTO(
                schelude.getCode(),
                request.getCodeAsignatureFk(), // Lo tomamos del request porque aún no hay fetch del subject
                schelude.getDia(),
                schelude.getHoraInicial(),
                schelude.getHoraFinal()
        );
    }

    // Método para parsear la hora en formato de 24h
    private LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new ConflictException("Formato de hora no válido. Utilice el formato 24h (HH:mm).");
        }
    }


    // Metodo para listar horarios en orden ascendente por el dia

    @Override
    public List<ListOrderScheludeDTO> listAllOrderSchelude() {
        List<Object[]> rawResults = repository.findAllOrderedSchedulesRaw();

        if (rawResults.isEmpty()) {
            throw new ConflictException("No hay horarios registrados.");
        }

        return rawResults.stream().map(obj -> new ListOrderScheludeDTO(
                (Integer) obj[0],
                (Integer) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                (String) obj[5],
                (String) obj[6]
        )).toList();
    }


    // Metodo para listar horarios de un estudiante por su ID

    @Override
    public List<ListOrderScheludeDTO> getOrderedScheludeByStudent(Integer studentId) {
        List<Object[]> rawResult = repository.findScheludeOrderedByDayForStudent(studentId);

        if (rawResult.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron horarios para el estudiante con ID: " + studentId);
        }

        return rawResult.stream().map(obj -> new ListOrderScheludeDTO(
                (Integer) obj[0],     // code
                (Integer) obj[1],     // codeSubject
                (String) obj[2],      // name
                (String) obj[3],      // dia
                (String) obj[4],      // horaInicial
                (String) obj[5],      // horaFinal
                (String) obj[6]       // nameTeacher
        )).toList();
    }


    // Metodo para actualizar un horario por su code

    @Override
    public ScheludeResponseDTO updateSchelude(UpdateScheludeRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaIni = parseTime(request.getHoraInicial());
        LocalTime horaFin = parseTime(request.getHoraFinal());

        if (horaIni.isAfter(horaFin)) {
            throw new ConflictException("La hora inicial no puede ser posterior a la hora final.");
        }

        // Buscar el horario por código
        Schelude schelude = repository.findAll().stream()
                .filter(s -> s.getCode().equals(request.getCode()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el horario con código: " + request.getCode()));

        // Validar que no haya cruces con otros horarios de la misma asignatura
        List<Schelude> existing = repository.findByCodeAsignatureFk(schelude.getCodeAsignatureFk());
        for (Schelude sch : existing) {
            if (!sch.getCode().equals(schelude.getCode()) && sch.getDia().equals(request.getDia())) {
                LocalTime existingStart = parseTime(sch.getHoraInicial());
                LocalTime existingEnd = parseTime(sch.getHoraFinal());

                boolean overlaps = !horaIni.isAfter(existingEnd) && !horaFin.isBefore(existingStart);
                if (overlaps) {
                    throw new ConflictException("El nuevo horario se cruza con otro ya existente.");
                }
            }
        }

        // Actualizar los datos
        schelude.setDia(request.getDia());
        schelude.setHoraInicial(request.getHoraInicial());
        schelude.setHoraFinal(request.getHoraFinal());
        repository.save(schelude);

        return new ScheludeResponseDTO(
                schelude.getCode(),
                schelude.getCodeAsignatureFk(),
                schelude.getDia(),
                schelude.getHoraInicial(),
                schelude.getHoraFinal()
        );
    }


    // Metodo para elimina run horario por su code
    @Override
    public InfoMessage deleteScheludeByCode(Integer code) {
        Schelude schelude = repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el horario con código: " + code));

        repository.delete(schelude);

        return new InfoMessage("El horario con código " + code + " fue eliminado exitosamente.", LocalDateTime.now());
    }




}
