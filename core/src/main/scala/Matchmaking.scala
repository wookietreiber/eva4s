/* **************************************************************************
 *                                                                          *
 *  Copyright (C)  2012  Nils Foken, Christian Krause                       *
 *                                                                          *
 *  Nils Foken        <nils.foken@it2009.ba-leipzig.de>                     *
 *  Christian Krause  <christian.krause@it2009.ba-leipzig.de>               *
 *                                                                          *
 ****************************************************************************
 *                                                                          *
 *  This file is part of 'scalevalgo'.                                      *
 *                                                                          *
 *  This project is free software: you can redistribute it and/or modify    *
 *  it under the terms of the GNU General Public License as published by    *
 *  the Free Software Foundation, either version 3 of the License, or       *
 *  any later version.                                                      *
 *                                                                          *
 *  This project is distributed in the hope that it will be useful,         *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 *  GNU General Public License for more details.                            *
 *                                                                          *
 *  You should have received a copy of the GNU General Public License       *
 *  along with this project. If not, see <http://www.gnu.org/licenses/>.    *
 *                                                                          *
 ****************************************************************************/


package ea

/** $matchmakinginfo */
object Matchmaking extends Matchmaking

/** $matchmakinginfo
  *
  * @define matchmakinginfo Contains default [[ea.Matchmaker]] implementations which define parental
  * selection.
  *
  * The idiomatic usage of the functions defined here is to input the parameters of the first
  * parameter list(s) and use the remaining function as a [[ea.Matchmaker]].
  *
  * @see [[ea.Matchmaker]]
  *
  * @define acceptance chance of a pair accepting the match
  * @define genome the type of the genome of the individuals
  * @define pairs the amount of pairs generated
  * @define parents the parents of the generation
  */
trait Matchmaking {

  // -----------------------------------------------------------------------
  // probabilistic matchmaking
  // -----------------------------------------------------------------------

  /** Returns a fixed amount of arbitrary pairs of individuals. This is the simplest form of
    * probabilistic matchmaking.
    *
    * @tparam G $genome
    *
    * @param pairs $pairs
    * @param parents $parents
    */
  def RandomForcedMatchmaker[G](pairs: Int)
                               (parents: Iterable[Individual[G]])
                                : Iterable[Pair[Individual[G],Individual[G]]] =
    Vector.fill(pairs) { parents.choosePair }

  /** Returns a varying amount of arbitrary pairs of individuals.
    *
    * @tparam G $genome
    *
    * @param pairs $pairs
    * @param acceptance $acceptance
    * @param parents $parents
    */
  def RandomAcceptanceMatchmaker[G](pairs: Int, acceptance: Double)
                                   (parents: Iterable[Individual[G]])
                                    : Iterable[Pair[Individual[G],Individual[G]]] =
    for (i ← 1 to pairs if Random.nextDouble < acceptance) yield parents choosePair

  /** Returns the fittest individuals of `pairs` tournaments.
    *
    * There will be `pairs` tournaments to determine the pairs. Each tournament consists of
    * `participants` randomly chosen participants. From these participants are the fittest two for
    * the pair chosen.
    *
    * @tparam G $genome
    *
    * @param pairs $pairs
    * @param participants amount of randomly selected individuals attending a tournament
    * @param parents $parents
    */
  def TournamentMatchmaker[G](pairs: Int, participants: Int)
                             (parents: Iterable[Individual[G]])
                              : Iterable[Pair[Individual[G],Individual[G]]] = {
    require(participants >= 2, "participants must be greater or equal to 2")

    Vector.fill(pairs) {
      val winners = parents choose participants sortBy { _.fitness } take 2
      Pair(winners(0), winners(1))
    }
  }

}
