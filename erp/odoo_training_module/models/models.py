# -*- coding: utf-8 -*-
import math

from odoo import models, fields, api

from datetime import date, datetime, time


class Course(models.Model):
    _name = 'training_ioc.course'
    _description = 'Courses'

    name = fields.Char('Name', required=True)
    level = fields.Selection([('basic', 'Basic'), ('intermediate', 'Intermediate'), ('advanced', 'Advanced')])
    num_hours = fields.Integer('Hours')
    description = fields.Text('Description')
    seminar_id = fields.One2many('training_ioc.seminar', 'course_id', string='Seminars')

    _sql_constraints = [
        ('name_uniq', 'unique (name)', "Tag name already exists !"),
    ]


class Seminar(models.Model):
    _name = 'training_ioc.seminar'
    _description = 'Seminars'

    name = fields.Char('Course', compute='_name_session')
    date_begin = fields.Date('Start', default=fields.datetime.now())
    date_end = fields.Date('End', default=fields.datetime.now())
    status = fields.Char('Status', compute='_seminar_status')
    num_hours = fields.Integer('Hours', related='course_id.num_hours')
    hours_per_session = fields.Float('Hours/Session', default='1')
    num_sessions = fields.Integer('Sessions', compute='_number_of_sessions')
    course_id = fields.Many2one('training_ioc.course', string='Course', required=True)
    trainer_id = fields.Many2one('res.partner', string='Trainer', domain=[('is_trainer', '=', True)])
    session_ids = fields.One2many('training_ioc.session', 'seminar_id', string='Session List')
    trained_employee = fields.Many2many('hr.employee', string='Employees')

    #    _sql_constraints = [('seminar_id_uniq', 'unique(seminar_id)')]
    # Calcula el número de sesiones. Si el resto es mayor que la mitad de una sesión, se añade una más
    @api.depends('num_hours', 'hours_per_session')
    def _number_of_sessions(self):
        num_sessions = 0
        for record in self:
            if record is not None:
                num_sessions = (math.floor(record.num_hours / record.hours_per_session))
                if (record.num_hours - (num_sessions * record.hours_per_session)) > (record.hours_per_session / 2):
                    num_sessions += 1
                record.num_sessions = num_sessions

    # Si al cambiar la fecha inicial, la fecha final de un seminario es anterior, la cambia por la inicial
    @api.onchange('date_begin', 'date_end')
    def _check_date_end(self):
        for record in self:
            if record is not None:
                if record.date_end < record.date_begin:
                    record.date_end = record.date_begin

    # Determina crea una etiqueta indicando si el seminario no ha comenzado, si está en curso o si ya terminó
    @api.depends('date_begin', 'date_end')
    def _seminar_status(self):
        for record in self:
            if record is not None:
                if record.date_begin > datetime.now().date():
                    record.status = 'Scheduled'
                elif record.date_end > datetime.now().date():
                    record.status = 'Ongoing'
                elif record.date_end < datetime.now().date():
                    record.status = 'Finished'
                else:
                    record.status = 'Unscheduled'

    # Genera el nombre del seminar
    @api.depends('course_id', 'date_begin')
    def _name_session(self):
        for record in self:
            if record is not None:
                dateVar = datetime.combine(record.date_begin, datetime.min.time())
                record.name = record.course_id.name + ' ' + dateVar.strftime("%b") + dateVar.strftime("%y")


class Session(models.Model):
    _name = 'training_ioc.session'
    _description = 'Session'

    name = fields.Char('Course', compute='_name_session')
    session_num = fields.Integer('Sessions', required=True)
    date_begin = fields.Date('Date', default=fields.datetime.now())
    time = fields.Float(string='Time')
    seminar_id = fields.Many2one('training_ioc.seminar', string='Seminar', required=True)

    # Genera el nombre de la sesión
    @api.depends('seminar_id', 'session_num')
    def _name_session(self):
        for record in self:
            if record is not None:
                record.name = record.seminar_id.name + ' S' + str(record.session_num)


class Expertise(models.Model):
    _name = 'training_ioc.expertise'
    _description = "Trainer Expertise"

    name = fields.Char('Expertise', required=True)

    _sql_constraints = [
        ('name_uniq', 'unique (name)', "Tag name already exists !"),
    ]
