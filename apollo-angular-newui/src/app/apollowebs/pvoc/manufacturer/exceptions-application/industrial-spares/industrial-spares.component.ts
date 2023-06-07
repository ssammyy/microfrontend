import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-industrial-spares',
    templateUrl: './industrial-spares.component.html',
    styleUrls: ['./industrial-spares.component.css']
})
export class IndustrialSparesComponent implements OnInit {
    sparesForm: FormGroup

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>) {
    }

    ngOnInit(): void {
        this.sparesForm = this.fb.group({
            "hsCode": ['', Validators.required],
            "industrialSpares": ['', Validators.required],
            "machineToFit": ['', Validators.required],
            "countryOfOrigin": ['', Validators.required]
        })
    }

    saveSpare() {
        this.dialogRef.close(this.sparesForm.value)
    }

}
