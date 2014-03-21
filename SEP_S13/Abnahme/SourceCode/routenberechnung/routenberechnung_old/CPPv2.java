package routenberechnung;

import java.util.*;
import Graph.*;

public class CPPv2 {

	/**
	 * Code aus dem Buch "A Java library of graph algorithms and optimization",
	 * Seite 151 bis 173 von T. Hang Lau, ISBN 1-584-88718-4
	 * 
	 */

	/**
	 * Erzeugt eine geschlossene Route die alle in der Liste enthaltenen Kanten
	 * enthält
	 * 
	 * @param kanten
	 *            - Eine Liste mit allen Kanten die durchlaufen werden soll
	 * 
	 * @param startpunkt
	 *            - der Startpunkt der Route
	 * 
	 * @return - Eine Liste mit einer Route durch alle Kanten in der Liste
	 * 
	 */
	public static ArrayList<Kante> getPath(List<Kante> kanten, Knoten startpunkt) {
		return getPath(Graph.graphFromKanten(kanten), startpunkt);
	}

	/**
	 * Erzeugt eine geschlossene Route die alle im Graph enthaltenen Kanten
	 * enthält
	 * 
	 * @param g
	 *            - Der Graph auf den der Algorithmus angewendet werden soll
	 * 
	 * @param startpunkt
	 *            - der Startpunkt der Route
	 * 
	 * @return - Eine Liste mit einer Route durch alle Kanten im Graph
	 * 
	 */
	public static ArrayList<Kante> getPath(Graph g, Knoten startpunkt) {

		setKnotenNummern(g);

		List<Kante> kanten = g.getAlleKanten();

		Map<Integer, Kante> kantenMap = new HashMap<Integer, Kante>();
		for (Kante k : kanten) {
			if (!k.getGesperrt())
				kantenMap.put(k.id, k);
		}

		Map<Integer, Knoten> knotenMap = new HashMap<Integer, Knoten>();
		for (Knoten k : g.getKnoten())
			knotenMap.put(k.getCppId(), k);

		int n = g.getKnoten().size();
		int m = kantenMap.size();
		int startnode = startpunkt.getCppId();

		int sol[][] = new int[m + 1][3];
		int trail[] = new int[m + m + 2];

		int nodei[] = new int[m + 1];
		int nodej[] = new int[m + 1];
		int cost[] = new int[m + 1];

		nodei[0] = 0;
		nodej[0] = 0;
		cost[0] = 0;

		for (Kante k1 : kantenMap.values()) {
			nodei[k1.id] = k1.getFrom().getCppId();
			nodej[k1.id] = k1.getTo().getCppId();
			cost[k1.id] = (int) (k1.getAbstand() * 100);
		}

		CPPv2.ChinesePostmanTour(n, m, startnode, nodei, nodej, cost, sol,
				trail);

		ArrayList<Kante> result = new ArrayList<Kante>();

		if (sol[0][0] != 0)
			System.out.println("Error code returned = " + sol[0][0]);
		else {
			// System.out.println("Optimal solution found.\n\nDuplicate edges:");
			// for (int i = 1; i <= sol[3][0]; i++) {
			// System.out.println(" " + sol[i][1] + " - " + sol[i][2]);
			// Knoten k1 = knotenMap.get(sol[i][1]);
			// Knoten k2 = knotenMap.get(sol[i][2]);
			// Kante k = k1.getKanteTo(k2);
			//
			// System.out.println("Kanten id: " + k1.getKanteTo(k2).id);
			// }
			// System.out.println("\nOptimal tour:");
			for (int i = 1; i < trail[0]; i++) {
				result.add(knotenMap.get(trail[i]).getKanteTo(
						knotenMap.get(trail[i + 1])));
			}
			// System.out.println("\n\nOptimal tour total cost = " + sol[1][0]);
			System.out.println("\n Laenge = " + result.size());
		}
		return result;
	}

	private static void setKnotenNummern(Graph g) {
		int id = 1;
		for (Knoten k : g.getKnoten()) {
			k.setCppId(id++);
		}
		id = 1;
		for (Kante k : g.getAlleKanten()) {
			if (!k.getGesperrt())
				k.id = id++;
		}
	}

	/**
	 * 
	 * @param n
	 *            - Anzahl der Knoten im Graphen
	 * @param m
	 *            - Anzahl der Kanten im Graphen
	 * @param startnode
	 *            - Der Startpunkt für den Pfad
	 * @param nodei
	 * 
	 * @param nodej
	 * @param cost
	 *            - Kosten für den die Kanten von nodei[i] --> nodej[i]
	 * @param sol
	 *            - Wenn sol[0][0] == 0 ist wurde eine optimalen Route gefunden
	 * 
	 * @param trail
	 *            - trail enthält die optimale route von trail[1] bist
	 *            trail[trail[0]]
	 */
	public static void ChinesePostmanTour(int n, int m, int startnode,
			int nodei[], int nodej[], int cost[], int sol[][], int trail[]) {
		int i, iplus1, j, k, idxa, idxb, idxc, idxd, idxe, wt, high, duparcs, totsolcost;
		int loch, loca, locb, locc, locd, loce, locf, locg, hub, tmpopty, tmpoptx = 0;
		int nplus, p, q, cur, curnext, position = 0;
		int neighbor[] = new int[m + m + 1];
		int weight[] = new int[m + m + 1];
		int degree[] = new int[n + 1];
		int next[] = new int[n + 2];
		int core[] = new int[n + 1];
		int aux1[] = new int[n + 1];
		int aux2[] = new int[n + 1];
		int aux3[] = new int[n + 1];
		int aux4[] = new int[n + 1];
		int aux5[] = new int[n + 1];
		int aux6[] = new int[n + 1];
		int tmparg[] = new int[1];
		float wk1[] = new float[n + 1];
		float wk2[] = new float[n + 1];
		float wk3[] = new float[n + 1];
		float wk4[] = new float[n + 1];
		float eps, work1, work2, work3, work4;
		boolean skip, complete;
		eps = 0.0001f;
		// check for connectedness <-- nicht nötig wird bereits VOR dem
		// algorithmus gemacht.
		// if (!connected(n, m, nodei, nodej)) {
		// sol[0][0] = 1;
		// return;
		// }
		sol[0][0] = 0;
		// store up the neighbors of each node
		for (i = 1; i <= n; i++)
			degree[i] = 0;
		for (j = 1; j <= m; j++) {
			degree[nodei[j]]++;
			degree[nodej[j]]++;
		}
		next[1] = 1;
		for (i = 1; i <= n; i++) {
			iplus1 = i + 1;
			next[iplus1] = next[i] + degree[i];
			degree[i] = 0;
		}
		totsolcost = 0;
		high = 0;
		for (j = 1; j <= m; j++) {
			totsolcost += cost[j];
			k = next[nodei[j]] + degree[nodei[j]];
			neighbor[k] = nodej[j];
			weight[k] = cost[j];
			degree[nodei[j]]++;
			k = next[nodej[j]] + degree[nodej[j]];
			neighbor[k] = nodei[j];
			weight[k] = cost[j];
			degree[nodej[j]]++;
			high += cost[j];
		}
		nplus = n + 1;
		locg = -nplus;
		for (i = 1; i <= n; i++)
			wk4[i] = high;
		// initialization
		for (p = 1; p <= n; p++) {
			core[p] = p;
			aux1[p] = p;
			aux4[p] = locg;
			aux5[p] = 0;
			aux3[p] = p;
			wk1[p] = 0f;
			wk2[p] = 0f;
			i = next[p];
			loch = next[p + 1];
			loca = loch - i;
			locd = loca / 2;
			locd *= 2;
			if (loca != locd) {
				loch--;
				aux4[p] = 0;
				wk3[p] = 0f;
				for (q = i; q <= loch; q++) {
					idxc = neighbor[q];
					work2 = (float) (weight[q]);
					if (wk4[idxc] > work2) {
						aux2[idxc] = p;
						wk4[idxc] = work2;
					}
				}
			}
		}
		// examine the labeling
		iterate: while (true) {
			work1 = high;
			for (locd = 1; locd <= n; locd++)
				if (core[locd] == locd) {
					work2 = wk4[locd];
					if (aux4[locd] >= 0) {
						work2 = 0.5f * (work2 + wk3[locd]);
						if (work1 >= work2) {
							work1 = work2;
							tmpoptx = locd;
						}
					} else {
						if (aux5[locd] > 0)
							work2 += wk1[locd];
						if (work1 > work2) {
							work1 = work2;
							tmpoptx = locd;
						}
					}
				}
			work4 = ((float) high) / 2f;
			if (work1 >= work4) {
				sol[0][0] = 2;
				return;
			}
			if (aux4[tmpoptx] >= 0) {
				idxb = aux2[tmpoptx];
				idxc = aux3[tmpoptx];
				loca = core[idxb];
				locd = tmpoptx;
				loce = loca;
				while (true) {
					aux5[locd] = loce;
					idxa = aux4[locd];
					if (idxa == 0)
						break;
					loce = core[idxa];
					idxa = aux5[loce];
					locd = core[idxa];
				}
				hub = locd;
				locd = loca;
				loce = tmpoptx;
				while (true) {
					if (aux5[locd] > 0)
						break;
					aux5[locd] = loce;
					idxa = aux4[locd];
					if (idxa == 0) {
						// augmentation
						loch = 0;
						for (locb = 1; locb <= n; locb++)
							if (core[locb] == locb) {
								idxd = aux4[locb];
								if (idxd >= 0) {
									if (idxd == 0)
										loch++;
									work2 = work1 - wk3[locb];
									wk3[locb] = 0f;
									wk1[locb] += work2;
									aux4[locb] = -idxd;
								} else {
									idxd = aux5[locb];
									if (idxd > 0) {
										work2 = wk4[locb] - work1;
										wk1[locb] += work2;
										aux5[locb] = -idxd;
									}
								}
							}
						while (true) {
							if (locd != loca) {
								loce = aux5[locd];
								aux5[locd] = 0;
								idxd = -aux5[loce];
								idxe = aux6[loce];
								aux4[locd] = -idxe;
								idxa = -aux4[loce];
								aux4[loce] = -idxd;
								locd = core[idxa];
							} else {
								if (loca == tmpoptx)
									break;
								aux5[loca] = 0;
								aux4[loca] = -idxc;
								aux4[tmpoptx] = -idxb;
								loca = tmpoptx;
								locd = hub;
							}
						}
						aux5[tmpoptx] = 0;
						idxa = 1;
						if (loch <= 2) {
							// generate the original graph by expanding all
							// pseudonodes
							wt = 0;
							for (locb = 1; locb <= n; locb++)
								if (core[locb] == locb) {
									idxb = -aux4[locb];
									if (idxb != nplus) {
										if (idxb >= 0) {
											loca = core[idxb];
											idxc = -aux4[loca];
											tmparg[0] = position;
											cpt_DuplicateEdges(neighbor, next,
													idxb, idxc, tmparg);
											position = tmparg[0];
											work1 = -(float) (weight[position]);
											work1 += wk1[locb] + wk1[loca];
											work1 += wk2[idxb] + wk2[idxc];
											if (Math.abs(work1) > eps) {
												sol[0][0] = 3;
												return;
											}
											wt += weight[position];
											aux4[loca] = idxb;
											aux4[locb] = idxc;
										}
									}
								}
							for (locb = 1; locb <= n; locb++) {
								while (true) {
									if (aux1[locb] == locb)
										break;
									hub = core[locb];
									loca = aux1[hub];
									idxb = aux5[loca];
									if (idxb > 0) {
										idxd = aux2[loca];
										locd = loca;
										tmparg[0] = locd;
										cpt_ExpandBlossom(core, aux1, aux3,
												wk1, wk2, tmparg, idxd);
										locd = tmparg[0];
										aux1[hub] = idxd;
										work3 = wk3[loca];
										wk1[hub] = work3;
										while (true) {
											wk2[idxd] -= work3;
											if (idxd == hub)
												break;
											idxd = aux1[idxd];
										}
										idxb = aux4[hub];
										locd = core[idxb];
										if (locd != hub) {
											loca = aux5[locd];
											loca = core[loca];
											idxd = aux4[locd];
											aux4[locd] = idxb;
											do {
												loce = core[idxd];
												idxb = aux5[loce];
												idxc = aux6[loce];
												locd = core[idxb];
												tmparg[0] = position;
												cpt_DuplicateEdges(neighbor,
														next, idxb, idxc,
														tmparg);
												position = tmparg[0];
												work1 = -(float) (weight[position]);
												wt += weight[position];
												work1 += wk1[locd] + wk1[loce];
												work1 += wk2[idxb] + wk2[idxc];
												if (Math.abs(work1) > eps) {
													sol[0][0] = 3;
													return;
												}
												aux4[loce] = idxc;
												idxd = aux4[locd];
												aux4[locd] = idxb;
											} while (locd != hub);
											if (loca == hub)
												continue;
										}
										while (true) {
											idxd = aux4[loca];
											locd = core[idxd];
											idxe = aux4[locd];
											tmparg[0] = position;
											cpt_DuplicateEdges(neighbor, next,
													idxd, idxe, tmparg);
											position = tmparg[0];
											wt += weight[position];
											work1 = -(float) (weight[position]);
											work1 += wk1[loca] + wk1[locd];
											work1 += wk2[idxd] + wk2[idxe];
											if (Math.abs(work1) > eps) {
												sol[0][0] = 3;
												return;
											}
											aux4[loca] = idxe;
											aux4[locd] = idxd;
											idxc = aux5[locd];
											loca = core[idxc];
											if (loca == hub)
												break;
										}
										break;
									} else {
										idxc = aux4[hub];
										aux1[hub] = hub;
										work3 = wk2[hub];
										wk1[hub] = 0f;
										wk2[hub] = 0f;
										do {
											idxe = aux3[loca];
											idxd = aux1[idxe];
											tmparg[0] = loca;
											cpt_ExpandBlossom(core, aux1, aux3,
													wk1, wk2, tmparg, idxd);
											loca = tmparg[0];
											loce = core[idxc];
											if (loce != loca) {
												idxb = aux4[loca];
												tmparg[0] = position;
												cpt_DuplicateEdges(neighbor,
														next, hub, idxb, tmparg);
												position = tmparg[0];
												work1 = -(float) (weight[position]);
												wt += weight[position];
												work1 += wk2[idxb] + wk1[loca]
														+ work3;
												if (Math.abs(work1) > eps) {
													sol[0][0] = 3;
													return;
												}
											} else
												aux4[loca] = idxc;
											loca = idxd;
										} while (loca != hub);
									}
								}
							}
							// store up the duplicate edges
							duparcs = 0;
							i = next[2];
							for (p = 2; p <= n; p++) {
								loch = next[p + 1] - 1;
								for (q = i; q <= loch; q++) {
									idxd = neighbor[q];
									if (idxd <= 0) {
										idxd = -idxd;
										if (idxd <= p) {
											duparcs++;
											sol[duparcs][1] = p;
											sol[duparcs][2] = idxd;
										}
									}
								}
								i = loch + 1;
							}
							cpt_Trail(n, neighbor, weight, next, aux3, core,
									startnode);
							// store up the optimal trail
							trail[1] = startnode;
							cur = startnode;
							curnext = 1;
							do {
								p = next[cur];
								q = aux3[cur];
								complete = true;
								for (i = q; i >= p; i--) {
									if (weight[i] > 0) {
										curnext++;
										trail[curnext] = weight[i];
										cur = weight[i];
										weight[i] = -1;
										complete = false;
										break;
									}
								}
							} while (!complete);
							trail[0] = curnext;
							sol[3][0] = duparcs;
							sol[1][0] = totsolcost + wt;
							return;
						}
						tmparg[0] = idxa;
						cpt_SecondScan(neighbor, weight, next, high, core,
								aux1, aux2, aux3, aux4, wk1, wk2, wk3, wk4,
								tmparg, n);
						idxa = tmparg[0];
						continue iterate;
					}
					loce = core[idxa];
					idxa = aux5[loce];
					locd = core[idxa];
				}
				while (true) {
					if (locd == hub) {
						// shrink a blossom
						work3 = wk1[hub] + work1 - wk3[hub];
						wk1[hub] = 0f;
						idxe = hub;
						do {
							wk2[idxe] += work3;
							idxe = aux1[idxe];
						} while (idxe != hub);
						idxd = aux1[hub];
						skip = false;
						if (hub != loca)
							skip = true;
						do {
							if (!skip) {
								loca = tmpoptx;
								loce = aux5[hub];
							}
							skip = false;
							while (true) {
								aux1[idxe] = loce;
								idxa = -aux4[loce];
								aux4[loce] = idxa;
								wk1[loce] += wk4[loce] - work1;
								idxe = loce;
								tmparg[0] = idxe;
								cpt_ShrinkBlossom(core, aux1, wk1, wk2, hub,
										tmparg);
								idxe = tmparg[0];
								aux3[loce] = idxe;
								locd = core[idxa];
								aux1[idxe] = locd;
								wk1[locd] += work1 - wk3[locd];
								idxe = locd;
								tmparg[0] = idxe;
								cpt_ShrinkBlossom(core, aux1, wk1, wk2, hub,
										tmparg);
								idxe = tmparg[0];
								aux3[locd] = idxe;
								if (loca == locd)
									break;
								loce = aux5[locd];
								aux5[locd] = aux6[loce];
								aux6[locd] = aux5[loce];
							}
							if (loca == tmpoptx) {
								aux5[tmpoptx] = idxb;
								aux6[tmpoptx] = idxc;
								break;
							}
							aux5[loca] = idxc;
							aux6[loca] = idxb;
						} while (hub != tmpoptx);
						aux1[idxe] = idxd;
						loca = aux1[hub];
						aux2[loca] = idxd;
						wk3[loca] = work3;
						aux5[hub] = 0;
						wk4[hub] = high;
						wk3[hub] = work1;
						cpt_FirstScan(neighbor, weight, next, core, aux1, aux2,
								aux3, aux4, wk1, wk2, wk3, wk4, hub);
						continue iterate;
					}
					locf = aux5[hub];
					aux5[hub] = 0;
					idxd = -aux4[locf];
					hub = core[idxd];
				}
			} else {
				if (aux5[tmpoptx] > 0) {
					loca = aux1[tmpoptx];
					if (loca != tmpoptx) {
						idxa = aux5[loca];
						if (idxa > 0) {
							// expand a blossom
							idxd = aux2[loca];
							locd = loca;
							tmparg[0] = locd;
							cpt_ExpandBlossom(core, aux1, aux3, wk1, wk2,
									tmparg, idxd);
							locd = tmparg[0];
							work3 = wk3[loca];
							wk1[tmpoptx] = work3;
							aux1[tmpoptx] = idxd;
							while (true) {
								wk2[idxd] -= work3;
								if (idxd == tmpoptx)
									break;
								idxd = aux1[idxd];
							}
							idxb = -aux4[tmpoptx];
							locd = core[idxb];
							idxc = aux4[locd];
							hub = core[idxc];
							if (hub != tmpoptx) {
								loce = hub;
								while (true) {
									idxa = aux5[loce];
									locd = core[idxa];
									if (locd == tmpoptx)
										break;
									idxa = aux4[locd];
									loce = core[idxa];
								}
								aux5[hub] = aux5[tmpoptx];
								aux5[tmpoptx] = aux6[loce];
								aux6[hub] = aux6[tmpoptx];
								aux6[tmpoptx] = idxa;
								idxd = aux4[hub];
								loca = core[idxd];
								idxe = aux4[loca];
								aux4[hub] = -idxb;
								locd = loca;
								while (true) {
									idxb = aux5[locd];
									idxc = aux6[locd];
									aux5[locd] = idxe;
									aux6[locd] = idxd;
									aux4[locd] = idxb;
									loce = core[idxb];
									idxd = aux4[loce];
									aux4[loce] = idxc;
									if (loce == tmpoptx)
										break;
									locd = core[idxd];
									idxe = aux4[locd];
									aux5[loce] = idxd;
									aux6[loce] = idxe;
								}
							}
							idxc = aux6[hub];
							locd = core[idxc];
							wk4[locd] = work1;
							if (locd != hub) {
								idxb = aux5[locd];
								loca = core[idxb];
								aux5[locd] = aux5[hub];
								aux6[locd] = idxc;
								do {
									idxa = aux4[locd];
									aux4[locd] = -idxa;
									loce = core[idxa];
									idxa = aux5[loce];
									aux5[loce] = -idxa;
									wk4[loce] = high;
									wk3[loce] = work1;
									locd = core[idxa];
									wk4[locd] = work1;
									cpt_FirstScan(neighbor, weight, next, core,
											aux1, aux2, aux3, aux4, wk1, wk2,
											wk3, wk4, loce);
								} while (locd != hub);
								aux5[hub] = aux6[loce];
								aux6[hub] = idxa;
								if (loca == hub)
									continue iterate;
							}
							loce = loca;
							do {
								idxa = aux4[loce];
								aux4[loce] = -idxa;
								locd = core[idxa];
								aux5[loce] = -locd;
								idxa = aux5[locd];
								aux4[locd] = -aux4[locd];
								loce = core[idxa];
								aux5[locd] = -loce;
							} while (loce != hub);
							do {
								locd = -aux5[loca];
								tmparg[0] = loca;
								cpt_SecondScan(neighbor, weight, next, high,
										core, aux1, aux2, aux3, aux4, wk1, wk2,
										wk3, wk4, tmparg, loca);
								loca = tmparg[0];
								loca = -aux5[locd];
								tmparg[0] = locd;
								cpt_SecondScan(neighbor, weight, next, high,
										core, aux1, aux2, aux3, aux4, wk1, wk2,
										wk3, wk4, tmparg, locd);
								locd = tmparg[0];
							} while (loca != hub);
							continue iterate;
						}
					}
					// modify a blossom
					wk4[tmpoptx] = high;
					wk3[tmpoptx] = work1;
					i = 1;
					wk1[tmpoptx] = 0f;
					idxa = -aux4[tmpoptx];
					loca = core[idxa];
					idxb = aux4[loca];
					if (idxb == tmpoptx) {
						i = 2;
						aux4[loca] = idxa;
						idxd = aux1[tmpoptx];
						aux1[tmpoptx] = loca;
						wk1[loca] += work1 - wk3[loca];
						idxe = loca;
						tmparg[0] = idxe;
						cpt_ShrinkBlossom(core, aux1, wk1, wk2, tmpoptx, tmparg);
						idxe = tmparg[0];
						aux3[loca] = idxe;
						aux1[idxe] = idxd;
						idxb = aux6[tmpoptx];
						if (idxb == tmpoptx) {
							idxa = aux5[tmpoptx];
							loca = core[idxa];
							aux4[tmpoptx] = aux4[loca];
							aux4[loca] = idxa;
							aux5[tmpoptx] = 0;
							idxd = aux1[tmpoptx];
							aux1[tmpoptx] = loca;
							wk1[loca] += work1 - wk3[loca];
							idxe = loca;
							tmparg[0] = idxe;
							cpt_ShrinkBlossom(core, aux1, wk1, wk2, tmpoptx,
									tmparg);
							idxe = tmparg[0];
							aux3[loca] = idxe;
							aux1[idxe] = idxd;
							cpt_FirstScan(neighbor, weight, next, core, aux1,
									aux2, aux3, aux4, wk1, wk2, wk3, wk4,
									tmpoptx);
							continue iterate;
						}
					}
					do {
						idxc = tmpoptx;
						locd = aux1[tmpoptx];
						while (true) {
							idxd = locd;
							idxe = aux3[locd];
							skip = false;
							while (true) {
								if (idxd == idxb) {
									skip = true;
									break;
								}
								if (idxd == idxe)
									break;
								idxd = aux1[idxd];
							}
							if (skip)
								break;
							locd = aux1[idxe];
							idxc = idxe;
						}
						idxd = aux1[idxe];
						aux1[idxc] = idxd;
						tmparg[0] = locd;
						cpt_ExpandBlossom(core, aux1, aux3, wk1, wk2, tmparg,
								idxd);
						locd = tmparg[0];
						wk4[locd] = work1;
						if (i == 2) {
							aux5[locd] = aux5[tmpoptx];
							aux6[locd] = idxb;
							aux5[tmpoptx] = 0;
							aux4[tmpoptx] = aux4[locd];
							aux4[locd] = -tmpoptx;
							cpt_FirstScan(neighbor, weight, next, core, aux1,
									aux2, aux3, aux4, wk1, wk2, wk3, wk4,
									tmpoptx);
							continue iterate;
						}
						i = 2;
						aux5[locd] = tmpoptx;
						aux6[locd] = aux4[locd];
						aux4[locd] = -idxa;
						idxb = aux6[tmpoptx];
						if (idxb == tmpoptx) {
							idxa = aux5[tmpoptx];
							loca = core[idxa];
							aux4[tmpoptx] = aux4[loca];
							aux4[loca] = idxa;
							aux5[tmpoptx] = 0;
							idxd = aux1[tmpoptx];
							aux1[tmpoptx] = loca;
							wk1[loca] += work1 - wk3[loca];
							idxe = loca;
							tmparg[0] = idxe;
							cpt_ShrinkBlossom(core, aux1, wk1, wk2, tmpoptx,
									tmparg);
							idxe = tmparg[0];
							aux3[loca] = idxe;
							aux1[idxe] = idxd;
							cpt_FirstScan(neighbor, weight, next, core, aux1,
									aux2, aux3, aux4, wk1, wk2, wk3, wk4,
									tmpoptx);
							continue iterate;
						}
					} while (core[idxb] == tmpoptx);
					aux5[locd] = aux5[tmpoptx];
					aux6[locd] = idxb;
					aux5[tmpoptx] = 0;
					locd = aux1[tmpoptx];
					if (locd == tmpoptx) {
						aux4[tmpoptx] = locg;
						tmpopty = tmpoptx;
						tmparg[0] = tmpopty;
						cpt_SecondScan(neighbor, weight, next, high, core,
								aux1, aux2, aux3, aux4, wk1, wk2, wk3, wk4,
								tmparg, tmpoptx);
						tmpopty = tmparg[0];
						continue iterate;
					}
					idxe = aux3[locd];
					idxd = aux1[idxe];
					aux1[tmpoptx] = idxd;
					tmparg[0] = locd;
					cpt_ExpandBlossom(core, aux1, aux3, wk1, wk2, tmparg, idxd);
					locd = tmparg[0];
					aux4[tmpoptx] = -aux4[locd];
					aux4[locd] = -tmpoptx;
					locc = locd;
					tmparg[0] = locc;
					cpt_SecondScan(neighbor, weight, next, high, core, aux1,
							aux2, aux3, aux4, wk1, wk2, wk3, wk4, tmparg, locd);
					locc = tmparg[0];
					tmpopty = tmpoptx;
					tmparg[0] = tmpopty;
					cpt_SecondScan(neighbor, weight, next, high, core, aux1,
							aux2, aux3, aux4, wk1, wk2, wk3, wk4, tmparg,
							tmpoptx);
					tmpopty = tmparg[0];
					continue iterate;
				} else {
					// grow an alternating tree
					idxa = -aux4[tmpoptx];
					if (idxa <= n) {
						aux5[tmpoptx] = aux2[tmpoptx];
						aux6[tmpoptx] = aux3[tmpoptx];
						loca = core[idxa];
						aux4[loca] = -aux4[loca];
						wk4[loca] = high;
						wk3[loca] = work1;
						cpt_FirstScan(neighbor, weight, next, core, aux1, aux2,
								aux3, aux4, wk1, wk2, wk3, wk4, loca);
						continue iterate;
					} else {
						idxb = aux2[tmpoptx];
						loca = core[idxb];
						aux4[tmpoptx] = aux4[loca];
						wk4[tmpoptx] = high;
						wk3[tmpoptx] = work1;
						aux4[loca] = idxb;
						wk1[loca] += work1 - wk3[loca];
						idxe = loca;
						tmparg[0] = idxe;
						cpt_ShrinkBlossom(core, aux1, wk1, wk2, tmpoptx, tmparg);
						idxe = tmparg[0];
						aux3[loca] = idxe;
						aux1[tmpoptx] = loca;
						aux1[idxe] = tmpoptx;
						cpt_FirstScan(neighbor, weight, next, core, aux1, aux2,
								aux3, aux4, wk1, wk2, wk3, wk4, tmpoptx);
						continue iterate;
					}
				}
			}
		}
	}

	static private void cpt_DuplicateEdges(int neighbor[], int next[],
			int idxb, int idxc, int tmparg[]) {
		/* this method is used internally by ChinesePostmanTour */
		// Duplicate matching edges
		int p, q, r;
		p = tmparg[0];
		q = idxb;
		r = idxc;
		while (true) {
			p = next[q];
			while (true) {
				if (neighbor[p] == r)
					break;
				p++;
			}
			neighbor[p] = -r;
			if (q == idxc)
				break;
			q = idxc;
			r = idxb;
		}
		tmparg[0] = p;
	}

	static private void cpt_ExpandBlossom(int core[], int aux1[], int aux3[],
			float wk1[], float wk2[], int tmparg[], int idxd) {
		/* this method is used internally by ChinesePostmanTour */
		// Expanding a blossom
		int p, q, r;
		float work;
		r = tmparg[0];
		p = r;
		do {
			r = p;
			q = aux3[r];
			work = wk1[r];
			while (true) {
				core[p] = r;
				wk2[p] -= work;
				if (p == q)
					break;
				p = aux1[p];
			}
			p = aux1[q];
			aux1[q] = r;
		} while (p != idxd);
		tmparg[0] = r;
	}

	static private void cpt_FirstScan(int neighbor[], int weight[], int next[],
			int core[], int aux1[], int aux2[], int aux3[], int aux4[],
			float wk1[], float wk2[], float wk3[], float wk4[], int locb) {
		/* this method is used internally by ChinesePostmanTour */
		// Node scanning
		int i, p, q, r, s, t, u, v;
		float work1, work2, work3, work4, work5;
		work3 = wk3[locb] - wk1[locb];
		q = locb;
		r = aux4[locb];
		t = -1;
		if (r > 0)
			t = core[r];
		do {
			i = next[q];
			v = next[q + 1] - 1;
			work1 = wk2[q];
			for (p = i; p <= v; p++) {
				s = neighbor[p];
				u = core[s];
				if (locb != u) {
					if (t != u) {
						work4 = wk4[u];
						work2 = wk1[u] + wk2[s];
						work5 = (float) (weight[p]);
						work5 += work3 - work1 - work2;
						if (work4 > work5) {
							wk4[u] = work5;
							aux2[u] = q;
							aux3[u] = s;
						}
					}
				}
			}
			q = aux1[q];
		} while (q != locb);
	}

	static private void cpt_SecondScan(int neighbor[], int weight[],
			int next[], int high, int core[], int aux1[], int aux2[],
			int aux3[], int aux4[], float wk1[], float wk2[], float wk3[],
			float wk4[], int tmparg[], int v) {
		/* this method is used internally by ChinesePostmanTour */
		// Node scanning
		int i, p, q, r, s, t, u;
		float work1, work2, work3, work4, work5;
		u = tmparg[0];
		do {
			r = core[u];
			if (r == u) {
				work4 = high;
				work2 = wk1[u];
				do {
					i = next[r];
					s = next[r + 1] - 1;
					work1 = wk2[r];
					for (p = i; p <= s; p++) {
						q = neighbor[p];
						t = core[q];
						if (t != u) {
							if (aux4[t] >= 0) {
								work3 = wk3[t] - wk1[t] - wk2[q];
								work5 = (float) (weight[p]);
								work5 += work3 - work2 - work1;
								if (work4 > work5) {
									work4 = work5;
									aux2[u] = q;
									aux3[u] = r;
								}
							}
						}
					}
					r = aux1[r];
				} while (r != u);
				wk4[u] = work4;
			}
			u++;
		} while (u <= v);
		tmparg[0] = u;
	}

	static private void cpt_ShrinkBlossom(int core[], int aux1[], float wk1[],
			float wk2[], int locb, int tmparg[]) {
		/* this method is used internally by ChinesePostmanTour */
		// Shrinking of a blossom
		int p, q, r;
		float work;
		p = tmparg[0];
		q = p;
		work = wk1[p];
		while (true) {
			core[p] = locb;
			wk2[p] += work;
			r = aux1[p];
			if (r == q) {
				tmparg[0] = p;
				return;
			}
			p = r;
		}
	}

	static private void cpt_Trail(int n, int neighbor[], int weight[],
			int next[], int aux3[], int core[], int startnode) {
		/* this method is used internally by ChinesePostmanTour */
		// Determine an Eulerian trail
		int i, nplus, p, q, r, t, u, v;
		boolean finish;
		nplus = n + 1;
		u = next[nplus];
		if (startnode <= 0 || startnode > n)
			startnode = 1;
		for (p = 1; p <= n; p++) {
			i = next[p] - 1;
			aux3[p] = i;
			core[p] = i;
		}
		p = startnode;
		iterate: while (true) {
			i = core[p];
			while (true) {
				v = next[p + 1] - 1;
				while (true) {
					i++;
					if (i > v)
						break;
					q = neighbor[i];
					if (q > n)
						continue;
					if (q >= 0) {
						t = core[q];
						do {
							t++;
						} while (neighbor[t] != p);
						neighbor[t] = nplus;
						t = aux3[q] + 1;
						aux3[q] = t;
						weight[t] = p;
						core[p] = i;
						p = q;
						continue iterate;
					}
					r = -p;
					q = -q;
					t = core[q];
					do {
						t++;
					} while (neighbor[t] != r);
					neighbor[t] = nplus;
					t = aux3[q] + 1;
					aux3[q] = t;
					weight[t] = p;
					t = aux3[p] + 1;
					aux3[p] = t;
					weight[t] = q;
				}
				core[p] = u;
				finish = true;
				for (p = 1; p <= n; p++) {
					i = core[p];
					t = aux3[p];
					if ((t >= next[p]) && (i < u)) {
						finish = false;
						break;
					}
				}
				if (finish)
					return;
			}
		}
	}
}
