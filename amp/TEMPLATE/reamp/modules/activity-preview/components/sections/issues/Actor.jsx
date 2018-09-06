import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
require('../../../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
export default class Actors extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    if (this.props.actor) {
      return <div className={'actors'}>{this.props.actor[AC.ACTOR_NAME] || ''}</div>;
    } else {
      return null;
    }
  }
}
