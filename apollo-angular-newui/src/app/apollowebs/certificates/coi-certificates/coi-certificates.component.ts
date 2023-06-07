import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-coi-certificates',
  templateUrl: './coi-certificates.component.html',
  styleUrls: ['./coi-certificates.component.css']
})
export class CoiCertificatesComponent implements OnInit {
  documentType = "coi"
  constructor() { }

  ngOnInit(): void {
  }

}
