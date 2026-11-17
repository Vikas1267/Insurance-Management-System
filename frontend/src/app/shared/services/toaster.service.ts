import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';

import { Observable } from 'rxjs/Observable';
import { of }         from 'rxjs/observable/of';
import 'rxjs/add/operator/delay';

@Injectable()
export class ToasterService {

  constructor(private snackBar: MatSnackBar) { }

  showToaster(message: string, action: string, duration:number):void{
    console.log("------------------------------"+message)
    this.snackBar.open(message, action, {
      duration: duration,
    });
  }

  openSuccessSnackBar(message: string,action: string, duration:number) {
    this.snackBar.open(message, action, {
      panelClass: ['mat--success'],
      verticalPosition: 'bottom',
      horizontalPosition: 'right',
      duration: duration
    });
  }

  openErrorSnackBar(message: string,action: string, duration:number) {
    this.snackBar.open(message, action, {
      panelClass: ['mat--errors'],
      verticalPosition: 'bottom',
      horizontalPosition: 'right',
      duration: duration
    });
  }

  delayMs = 500;

  getHeroes(): Observable<Hero[]> {
    return of(heroes).delay(this.delayMs); // simulate latency with delay
  }

  updateHero(hero: Hero): Observable<Hero>  {
    const oldHero = heroes.find(h => h.id === hero.id);
    const newHero = Object.assign(oldHero, hero); // Demo: mutate cached hero
    return of(newHero).delay(this.delayMs); // simulate latency with delay
  }

}

export class Hero {
  id = 0;
  name = '';
  addresses: Address[];
}

export class Address {
  street = '';
  city   = '';
  state  = '';
  zip    = '';
}

export const heroes: Hero[] = [
  {
    id: 1,
    name: 'Whirlwind',
    addresses: [
      {street: '123 Main',  city: 'Anywhere', state: 'CA',  zip: '94801'},
      {street: '456 Maple', city: 'Somewhere', state: 'VA', zip: '23226'},
    ]
  },
  {
    id: 2,
    name: 'Bombastic',
    addresses: [
      {street: '789 Elm',  city: 'Smallville', state: 'OH',  zip: '04501'},
    ]
  },
  {
    id: 3,
    name: 'Magneta',
    addresses: [ ]
  },
];

export const states = ['CA', 'MD', 'OH', 'VA'];


