import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-ncr-cor-certificate',
  templateUrl: './ncr-cor-certificate.component.html',
  styleUrls: ['./ncr-cor-certificate.component.css']
})
export class NcrCorCertificateComponent implements OnInit {
  documentType = "ncr-cor"

  constructor() {
  }

  ngOnInit(): void {
  }

}
